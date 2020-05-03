package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.CarManagementService;
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

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private SiteDAO siteDAO;

    @Autowired
    private SiteManagementService siteManagementService;

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private CarManagementService carManagementService;

    @Autowired
    private PlayerDAO playerDAO;

    @Transactional
    @Override
    public void saveMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException {
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
    }

    @Transactional
    @Override
    public void registerPlayer(Player player, Match match, Car car, UserContext userContext)
            throws ApplicationException, DatabaseException {
        if (player == null || player.getId() == null || match == null || match.getId() == null) {
            throw new ApplicationException("invalid.user.error", "Invalid arguments to join a match");
        }

        if (match.getNumPlayersMax() != null && match.getNumRegisteredPlayers() >= match.getNumPlayersMax()) {
            throw new ApplicationException("max.players.match.error", "This match is not accepting more registrations");
        }

        boolean isCarConfirmed = false;

        if (car != null) {
            processCarToJoinMatch(car, userContext);

            if (car.getDriver() != null && car.getDriver().equals(player)) {
                // No confirmation mail needed for the driver of a car
                isCarConfirmed = true;
            } else {
                // A confirmation is needed from the driver of the car selected for this operation
                isCarConfirmed = false;
                processCarSeatRequest(match, player, car, userContext);
            }
        }

        if (!match.isPlayerRegistered(player)) {
            matchDAO.registerPlayer(player, match, car, isCarConfirmed);
            log.info("Player {} successfully registered to the match {}", player.getId(), match.getId());
        } else {
            throw new ApplicationException("player.already.registered.error", "Player already registered in match");
        }
    }

    @Transactional
    @Override
    public void unregisterPlayer(Player player, Match match, UserContext userContext) throws DatabaseException, ApplicationException {
        if (player == null || player.getId() == null || match == null || match.getCode() == null) {
            throw new ApplicationException("Invalid arguments to quit a match");
        }

        if (match.isPlayerRegistered(player)) {
            matchDAO.unregisterPlayer(player, match);
            log.info("Player unregistered from match");
        } else {
            throw new ApplicationException("Player not registered in this match");
        }
    }

    @Transactional
    @Override
    public void unregisterPlayerFromAllMatches(Player player, UserContext userContext) throws DatabaseException {
        int numMatches = matchDAO.unregisterPlayerFromAllMatches(player);
        log.info("Player unregistered from {} matches", numMatches);
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
     * @throws DatabaseException
     */
    private void processCarToJoinMatch(Car car, UserContext userContext) throws DatabaseException {
        if (car.getId() != null) {
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
     * If the carpooling feature is enabled for the match passed in parameters, it sends an email message requesting to
     * the driver of the car passed in parameters for a place in his/her car for this match
     *
     * @param match The match selected by the player
     * @param player Player asking for a seat
     * @param car Car selected by the player
     * @param userContext
     */
    private void processCarSeatRequest(Match match, Player player, Car car, UserContext userContext) {
        // TODO Implement this method
    }
}
