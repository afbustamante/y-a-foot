package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.CarpoolingService;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.SiteManagementService;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * @author andresbustamante
 */
@Service
public class MatchManagementServiceImpl implements MatchManagementService {

    private static final Integer CODE_LENGTH = 10;

    private final RandomStringGenerator codeGenerator = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private final Logger log = LoggerFactory.getLogger(MatchManagementServiceImpl.class);

    private MatchDAO matchDAO;

    private SiteDAO siteDAO;

    private SiteManagementService siteManagementService;

    private CarDAO carDAO;

    private CarManagementService carManagementService;

    private CarpoolingService carpoolingService;

    private PlayerDAO playerDAO;

    @Autowired
    public MatchManagementServiceImpl(MatchDAO matchDAO, SiteDAO siteDAO, CarDAO carDAO, PlayerDAO playerDAO,
                                      SiteManagementService siteManagementService,
                                      CarManagementService carManagementService, CarpoolingService carpoolingService) {
        this.matchDAO = matchDAO;
        this.siteDAO = siteDAO;
        this.siteManagementService = siteManagementService;
        this.carDAO = carDAO;
        this.carManagementService = carManagementService;
        this.carpoolingService = carpoolingService;
        this.playerDAO = playerDAO;
    }

    @Transactional
    @Override
    public Integer saveMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException {
        String matchCode;
        boolean isCodeAlreadyInUse;
        do {
            matchCode = generateMatchCode();
            isCodeAlreadyInUse = matchDAO.isCodeAlreadyRegistered(matchCode);
        } while (isCodeAlreadyInUse);

        match.setCode(matchCode);
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
        boolean isUserAuthorised = ctx.getUsername().equals(match.getCreator().getEmail()) || ctx.getUsername().equals(player.getEmail());

        if (isUserAuthorised &&  match.isPlayerRegistered(player)) {
            matchDAO.unregisterPlayer(player, match);
            log.info("Player #{} was unregistered from match #{}", player.getId(), match.getId());
        } else {
            throw new ApplicationException("Player not registered in this match");
        }
    }

    @Transactional
    @Override
    public void unregisterPlayerFromAllMatches(Player player, UserContext userContext) throws DatabaseException {
        int numMatches = matchDAO.unregisterPlayerFromAllMatches(player);
        log.info("Player #{} unregistered from {} matches", player.getId(), numMatches);
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
}
