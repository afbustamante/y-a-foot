package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.core.events.CarpoolingRequestEvent;
import net.andresbustamante.yafoot.core.events.MatchPlayerRegistrationEvent;
import net.andresbustamante.yafoot.core.events.MatchPlayerUnsubscriptionEvent;
import net.andresbustamante.yafoot.core.exceptions.PastMatchException;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Registration;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import net.andresbustamante.yafoot.core.services.MatchManagementService;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.CANCELLED;
import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.CREATED;

/**
 * @author andresbustamante
 */
@Service
public class MatchManagementServiceImpl implements MatchManagementService {

    private static final Integer CODE_LENGTH = 10;

    private final RandomStringGenerator codeGenerator =
            new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private final Logger log = LoggerFactory.getLogger(MatchManagementServiceImpl.class);

    private final MatchDao matchDAO;
    private final SiteDao siteDAO;
    private final SiteManagementService siteManagementService;
    private final CarDao carDAO;
    private final CarManagementService carManagementService;
    private final CarpoolingService carpoolingService;
    private final PlayerDao playerDAO;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.queues.matches.registrations.name}")
    private String matchPlayerRegistrationsQueue;

    @Value("${app.messaging.queues.matches.unsubscriptions.name}")
    private String matchPlayerUnregistrationsQueue;

    @Value("${app.messaging.queues.carpooling.requests.name}")
    private String carpoolingRequestsQueue;

    @Autowired
    public MatchManagementServiceImpl(
            final MatchDao matchDAO, final SiteDao siteDAO, final CarDao carDAO, final PlayerDao playerDAO,
            final SiteManagementService siteManagementService, final CarManagementService carManagementService,
            final CarpoolingService carpoolingService, final RabbitTemplate rabbitTemplate) {
        this.matchDAO = matchDAO;
        this.siteDAO = siteDAO;
        this.siteManagementService = siteManagementService;
        this.carDAO = carDAO;
        this.carManagementService = carManagementService;
        this.carpoolingService = carpoolingService;
        this.playerDAO = playerDAO;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @Override
    public Integer saveMatch(final Match match, final UserContext userContext)
            throws DatabaseException, ApplicationException {
        String matchCode;

        if (match.getDate().isBefore(LocalDateTime.now())) {
            throw new ApplicationException("match.past.new.date.error", "A match cannot be planned in the past");
        }

        boolean isCodeAlreadyInUse;
        do {
            matchCode = generateMatchCode();
            isCodeAlreadyInUse = matchDAO.isCodeAlreadyRegistered(matchCode);
        } while (isCodeAlreadyInUse);

        match.setCode(matchCode);
        match.setStatus(CREATED);
        match.setRegistrations(new ArrayList<>());

        final Player creator = processCreatorToCreateMatch(match, userContext);

        processSiteToCreateMatch(match, userContext);

        matchDAO.saveMatch(match);
        log.info("New match registered with the ID number {}", match.getId());

        registerPlayer(creator, match, null, userContext);
        return match.getId();
    }

    @Transactional(rollbackFor = {ApplicationException.class, DatabaseException.class})
    @Override
    public void registerPlayer(final Player player, final Match match, final Car car, final UserContext userContext)
            throws ApplicationException, DatabaseException {
        if (!match.isAcceptingRegistrations()) {
            throw new ApplicationException("max.players.match.error", "This match is not accepting more registrations");
        } else if (match.isAcceptingRegistrations() && match.isPlayerRegistered(player)) {
            processCarpoolingImpacts(player, match, car, userContext);

            // Remove the existing registry to insert a new one with fresh information
            matchDAO.unregisterPlayer(player, match);
        }

        if (car != null) {
            boolean isCarConfirmed = processCarToJoinMatch(car, userContext);
            matchDAO.registerPlayer(player, match, car, isCarConfirmed);

            notifyRegisteredPlayer(player, match);

            if (match.isCarpoolingEnabled() && !isCarConfirmed) {
                // A confirmation is needed from the driver of the car selected for this operation
                carpoolingService.processCarSeatRequest(match, player, car, userContext);

                notifyCarpoolRequest(player, match, car);
            }
        } else {
            matchDAO.registerPlayer(player, match, null, null);
        }

        log.info("Player {} successfully registered to the match {}", player.getId(), match.getId());
    }

    /**
     * If carpooling is enabled for a match, it checks if carpooling is impacted by the new registration request
     * meaning that an existing player is changing his/her transportation options.
     *
     * @param player Player to check
     * @param match Match to check
     * @param car Car to process
     * @param userContext Context of the user making the registration
     * @throws ApplicationException
     */
    private void processCarpoolingImpacts(final Player player, final Match match, final Car car,
                                          final UserContext userContext)
            throws ApplicationException {
        if (match.isCarpoolingEnabled()) {
            // Check if an update of carpooling must be made when a driver changes of transportation option
            Registration oldRegistration = matchDAO.loadRegistration(match, player);

            if (oldRegistration != null && oldRegistration.getCar() != null && player.equals(
                    oldRegistration.getCar().getDriver())) {
                // The driver already registered is changing of mind
                carpoolingService.processTransportationChange(match, oldRegistration.getCar(), car, userContext);
            }
        }
    }

    @Transactional
    @Override
    public void unregisterPlayer(final Player player, final Match match, final UserContext ctx)
            throws DatabaseException, ApplicationException {
        // Two players are authorised to unregister a player: himself/herself or the player who created the match
        boolean isUserAuthorised = ctx.getUsername().equals(match.getCreator().getEmail()) || ctx.getUsername().equals(
                player.getEmail());

        if (isUserAuthorised &&  match.isPlayerRegistered(player)) {
            if (match.isCarpoolingEnabled()) {
                processCarpoolingImpactsAfterAbandon(player, match, ctx);
            }
            matchDAO.unregisterPlayer(player, match);
            log.info("Player #{} was unregistered from match #{}", player.getId(), match.getId());

            notifyUnregisteredPlayer(player, match);
        } else {
            throw new ApplicationException("unknown.player.registration.error", "Player not registered in this match");
        }
    }

    /**
     * Update carpooling information on registration impacted by the abandon of a given player from a given match.
     * If the player is a driver from a car being confirmed for other players, the system updates their registrations
     * according to this abandon.
     *
     * @param player Player quitting the match
     * @param match Match being abandoned by the player
     * @param ctx User context
     */
    private void processCarpoolingImpactsAfterAbandon(final Player player, final Match match, final UserContext ctx)
            throws DatabaseException, ApplicationException {
        List<Car> registeredCars = carpoolingService.findAvailableCarsByMatch(match);

        if (CollectionUtils.isNotEmpty(registeredCars)) {
            Optional<Car> ownedCar = registeredCars.stream().filter(
                    car -> car.getDriver().equals(player)
            ).findFirst();

            if (ownedCar.isPresent()) {
                // The system found a car driven by the player
                List<Registration> impactedRegistrations = matchDAO.findPassengerRegistrationsByCar(match,
                        ownedCar.get());

                if (CollectionUtils.isNotEmpty(impactedRegistrations)) {
                    // At least one player asked or was confirmed for a seat in this car
                    // Update carpooling information to remove confirmations on this car
                    for (Registration registration : impactedRegistrations) {
                        carpoolingService.updateCarpoolingInformation(match, registration.getPlayer(), ownedCar.get(),
                                false, ctx);
                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public void unregisterPlayerFromAllMatches(final Player player, final UserContext userContext) {
        int numMatches = matchDAO.unregisterPlayerFromAllMatches(player);
        log.info("Player #{} unregistered from {} matches", player.getId(), numMatches);
    }

    @Transactional
    @Override
    public void cancelMatch(final Match match, final UserContext userContext) throws ApplicationException {
        if (match.getDate().isBefore(LocalDateTime.now())) {
            throw new PastMatchException("It is not possible to cancel a match in the past");
        } else if (match.getCreator() == null || !match.getCreator().getEmail().equals(userContext.getUsername())) {
            throw new UnauthorisedUserException("This match can only be cancelled by its creator");
        }

        match.setStatus(CANCELLED);
        matchDAO.updateMatchStatus(match);
        log.info("Match {} with code {} successfully cancelled", match.getId(), match.getCode());
    }

    private Player processCreatorToCreateMatch(final Match match, final UserContext userContext)
            throws DatabaseException {
        Player creator = playerDAO.findPlayerByEmail(userContext.getUsername());

        if (creator != null) {
            match.setCreator(creator);
        } else {
            throw new DatabaseException("User not found in DB: " + userContext.getUsername());
        }
        return creator;
    }

    private void processSiteToCreateMatch(final Match match, final UserContext userContext) throws DatabaseException {
        if (match.getSite().getId() != null) {
            Site storedSite = siteDAO.findSiteById(match.getSite().getId());

            if (storedSite != null) {
                match.setSite(storedSite);
            } else {
                throw new DatabaseException("Site not found in DB: " + match.getSite().getId());
            }
        } else {
            siteManagementService.saveSite(match.getSite(), userContext);
        }
    }

    /**
     * Loads or saves the car used to join a match.
     *
     * @param car The car used by the registration
     * @param userContext
     * @return Indicates if the car is confirmed for the current player after processing the car
     * @throws DatabaseException
     */
    private boolean processCarToJoinMatch(final Car car, final UserContext userContext) throws DatabaseException {
        if (car.getId() != null) {
            // Look for the car in DB
            Car storedCar = carDAO.findCarById(car.getId());

            if (storedCar == null) {
                throw new DatabaseException("Car not found in DB: " + car.getId());
            } else {
                car.setName(storedCar.getName());
                car.setNumSeats(storedCar.getNumSeats());
                car.setDriver(storedCar.getDriver());
            }
        } else {
            // Save the new car
            carManagementService.saveCar(car, userContext);
        }

        // Case 1: No confirmation needed for the driver of a car. This car is confirmed
        // Case 2: This car doesn't belong to the user registering to the match. He/She is not confirmed yet
        return car.getDriver() != null && car.getDriver().getEmail().equals(userContext.getUsername());
    }

    /**
     * Generates a new alphabetic code for a match by using the maximum length as marked in CODE_LENGTH.
     *
     * @return New alphabetic random code
     */
    private String generateMatchCode() {
        log.info("Generating new match code");
        return codeGenerator.generate(CODE_LENGTH);
    }

    /**
     * Sends a notification to the message broker after for this action.
     *
     * @param player Player that joined the match
     * @param match The match to update
     */
    private void notifyRegisteredPlayer(final Player player, final Match match) {
        MatchPlayerRegistrationEvent event = new MatchPlayerRegistrationEvent();
        event.setPlayerFirstName(player.getFirstName());
        event.setPlayerId(player.getId());
        event.setMatchId(match.getId());
        event.setMatchCode(match.getCode());

        rabbitTemplate.convertAndSend(matchPlayerRegistrationsQueue, event);
    }

    /**
     * Sends a notification to the message broker after for this action.
     *
     * @param player Player that left the match
     * @param match The match to update
     */
    private void notifyUnregisteredPlayer(final Player player, final Match match) {
        MatchPlayerUnsubscriptionEvent event = new MatchPlayerUnsubscriptionEvent();
        event.setPlayerFirstName(player.getFirstName());
        event.setPlayerId(player.getId());
        event.setMatchId(match.getId());
        event.setMatchCode(match.getCode());

        rabbitTemplate.convertAndSend(matchPlayerUnregistrationsQueue, event);
    }

    /**
     * Sends a notification of carpooling from a player for a given match in a given car.
     *
     * @param player Player asking for carpooling
     * @param match Match to ask for
     * @param car Selected car for the request
     */
    private void notifyCarpoolRequest(final Player player, final Match match, final Car car) {
        CarpoolingRequestEvent event = new CarpoolingRequestEvent();
        event.setMatchId(match.getId());
        event.setMatchCode(match.getCode());
        event.setPlayerId(player.getId());
        event.setPlayerFirstName(player.getFirstName());
        event.setCarId(car.getId());
        event.setCarName(car.getName());

        rabbitTemplate.convertAndSend(carpoolingRequestsQueue, event);
    }
}
