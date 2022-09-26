package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.CarDAO;
import net.andresbustamante.yafoot.core.dao.MatchDAO;
import net.andresbustamante.yafoot.core.dao.PlayerDAO;
import net.andresbustamante.yafoot.core.dao.SiteDAO;
import net.andresbustamante.yafoot.core.exceptions.PastMatchException;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.*;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import net.andresbustamante.yafoot.core.services.MatchManagementService;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.CANCELLED;
import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.CREATED;

/**
 * @author andresbustamante
 */
@Service
public class MatchManagementServiceImpl implements MatchManagementService {

    private static final Integer CODE_LENGTH = 10;

    private final RandomStringGenerator codeGenerator = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private final Logger log = LoggerFactory.getLogger(MatchManagementServiceImpl.class);

    private final MatchDAO matchDAO;
    private final SiteDAO siteDAO;
    private final SiteManagementService siteManagementService;
    private final CarDAO carDAO;
    private final CarManagementService carManagementService;
    private final CarpoolingService carpoolingService;
    private final PlayerDAO playerDAO;
    private final MessagingService messagingService;

    @Autowired
    public MatchManagementServiceImpl(MatchDAO matchDAO, SiteDAO siteDAO, CarDAO carDAO, PlayerDAO playerDAO,
                                      SiteManagementService siteManagementService, MessagingService messagingService,
                                      CarManagementService carManagementService, CarpoolingService carpoolingService) {
        this.matchDAO = matchDAO;
        this.siteDAO = siteDAO;
        this.siteManagementService = siteManagementService;
        this.messagingService = messagingService;
        this.carDAO = carDAO;
        this.carManagementService = carManagementService;
        this.carpoolingService = carpoolingService;
        this.playerDAO = playerDAO;
    }

    @Transactional
    @Override
    public Integer saveMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException {
        String matchCode;

        if (match.getDate().isBefore(OffsetDateTime.now())) {
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

        Player creator = processCreatorToCreateMatch(match, userContext);

        processSiteToCreateMatch(match, userContext);

        matchDAO.saveMatch(match);
        log.info("New match registered with the ID number {}", match.getId());

        registerPlayer(creator, match, null, userContext);
        return match.getId();
    }

    @Transactional(rollbackFor = {ApplicationException.class, DatabaseException.class})
    @Override
    public void registerPlayer(Player player, Match match, Car car, UserContext userContext)
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

            if (match.isCarpoolingEnabled() && !isCarConfirmed) {
                // A confirmation is needed from the driver of the car selected for this operation
                carpoolingService.processCarSeatRequest(match, player, car, userContext);
            }
        } else {
            matchDAO.registerPlayer(player, match, null, null);
        }

        log.info("Player {} successfully registered to the match {}", player.getId(), match.getId());
    }

    /**
     * If carpooling is enabled for a match, it checks if carpooling is impacted by the new registration request
     * meaning that an existing player is changing his/her transportation options
     *
     * @param player Player to check
     * @param match Match to check
     * @param car Car to process
     * @param userContext Context of the user making the registration
     * @throws ApplicationException
     */
    private void processCarpoolingImpacts(Player player, Match match, Car car, UserContext userContext) throws ApplicationException {
        if (match.isCarpoolingEnabled()) {
            // Check if an update of carpooling must be made when a driver changes of transportation option
            Registration oldRegistration = matchDAO.loadRegistration(match, player);

            if (oldRegistration != null && oldRegistration.getCar() != null && player.equals(oldRegistration.getCar().getDriver())) {
                // The driver already registered is changing of mind
                carpoolingService.processTransportationChange(match, oldRegistration.getCar(), car, userContext);
            }
        }
    }

    @Transactional
    @Override
    public void unregisterPlayer(Player player, Match match, UserContext ctx) throws DatabaseException, ApplicationException {
        // Two players are authorised to unregister a player: himself/herself or the player who created the match
        boolean isUserAuthorised = ctx.getUsername().equals(match.getCreator().getEmail()) || ctx.getUsername().equals(player.getEmail());
        // A match is impacted when the last player before arriving to the lowest number of players expected quits
        boolean isMatchImpacted = match.getNumRegisteredPlayers().equals(match.getNumPlayersMin());

        if (isUserAuthorised &&  match.isPlayerRegistered(player)) {
            if (match.isCarpoolingEnabled()) {
                processCarpoolingImpactsAfterAbandon(player, match, ctx);
            }
            matchDAO.unregisterPlayer(player, match);
            log.info("Player #{} was unregistered from match #{}", player.getId(), match.getId());

            if (isMatchImpacted) {
                // Send an alert as the match has a number of players below the minimum expected now
                sendAlertMessage(match);
            }
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
     */
    private void processCarpoolingImpactsAfterAbandon(Player player, Match match, UserContext ctx) throws DatabaseException, ApplicationException {
        List<Car> registeredCars = carpoolingService.findAvailableCarsByMatch(match);

        if (CollectionUtils.isNotEmpty(registeredCars)) {
            Optional<Car> ownedCar = registeredCars.stream().filter(
                    car -> car.getDriver().equals(player)
            ).findFirst();

            if (ownedCar.isPresent()) {
                // The system found a car driven by the player
                List<Registration> impactedRegistrations = matchDAO.findPassengerRegistrationsByCar(match, ownedCar.get());

                if (CollectionUtils.isNotEmpty(impactedRegistrations)) {
                    // At least one player asked or was confirmed for a seat in this car
                    // Update carpooling information to remove confirmations on this car
                    for (Registration registration : impactedRegistrations) {
                        carpoolingService.updateCarpoolingInformation(match, registration.getPlayer(), ownedCar.get(), false, ctx);
                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public void unregisterPlayerFromAllMatches(Player player, UserContext userContext) throws DatabaseException {
        int numMatches = matchDAO.unregisterPlayerFromAllMatches(player);
        log.info("Player #{} unregistered from {} matches", player.getId(), numMatches);
    }

    @Transactional
    @Override
    public void cancelMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException {
        if (match.getDate().isBefore(OffsetDateTime.now())) {
            throw new PastMatchException("It is not possible to cancel a match in the past");
        } else if (match.getCreator() == null || !match.getCreator().getEmail().equals(userContext.getUsername())) {
            throw new UnauthorisedUserException("This match can only be cancelled by its creator");
        }

        match.setStatus(CANCELLED);
        matchDAO.updateMatchStatus(match);
        log.info("Match {} with code {} successfully cancelled", match.getId(), match.getCode());
    }

    private Player processCreatorToCreateMatch(Match match, UserContext userContext) throws DatabaseException {
        Player creator = playerDAO.findPlayerByEmail(userContext.getUsername());

        if (creator != null) {
            match.setCreator(creator);
        } else {
            throw new DatabaseException("User not found in DB: " + userContext.getUsername());
        }
        return creator;
    }

    private void processSiteToCreateMatch(Match match, UserContext userContext) throws DatabaseException {
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
     * Loads or saves the car used to join a match
     *
     * @param car The car used by the registration
     * @param userContext
     * @return Indicates if the car is confirmed for the current player after processing the car
     * @throws DatabaseException
     */
    private boolean processCarToJoinMatch(Car car, UserContext userContext) throws DatabaseException {
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
        return (car.getDriver() != null) && (car.getDriver().getEmail().equals(userContext.getUsername()));
    }

    /**
     * Generates a new alphabetic code for a match by using the maximum length as marked in CODE_LENGTH
     *
     * @return New alphabetic random code
     */
    private String generateMatchCode() {
        log.info("Generating new match code");
        return codeGenerator.generate(CODE_LENGTH);
    }

    /**
     * Sends an alert message to the player who created the match to prevent him from an action leading a match to an
     * imminent danger of being cancelled
     *
     * @param match The match concerned by the alert
     * @throws ApplicationException If a problem comes when sending the alert
     */
    private void sendAlertMessage(Match match) throws ApplicationException {
        String[] parameters = {match.getCode()};
        String language = match.getCreator().getPreferredLanguage();
        Locale locale = new Locale(language);
        String template = "match-cancel-risk-alert-email_" + language + ".ftl";

        MatchAlert alert = new MatchAlert();
        alert.setCreatorFirstName(match.getCreator().getFirstName());
        alert.setMatchDate(match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", locale)));
        alert.setNumMinPlayers(match.getNumPlayersMin());

        messagingService.sendEmail(match.getCreator().getEmail(), "match.cancel.risk.alert.subject", parameters,
                template, alert, locale);
    }
}
