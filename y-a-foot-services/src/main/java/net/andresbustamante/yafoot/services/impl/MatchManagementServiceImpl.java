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
        String codeMatch;
        boolean codeDejaUtilise;
        do {
            codeMatch = generateMatchCode();
            codeDejaUtilise = matchDAO.isCodeAlreadyRegistered(codeMatch);
        } while (codeDejaUtilise);

        match.setCode(codeMatch);
        match.setRegistrations(new ArrayList<>());

        Player createur = playerDAO.findPlayerByEmail(userContext.getUsername());

        if (createur != null) {
            match.setCreator(createur);
        } else {
            throw new DatabaseException("User not found in DB: " + userContext.getUsername());
        }

        if (match.getSite().getId() != null) {
            Site siteExistant = siteDAO.findSiteById(match.getSite().getId());

            if (siteExistant != null) {
                match.setSite(siteExistant);
            } else {
                // Créer aussi le site
                siteManagementService.saveSite(match.getSite(), userContext);
            }
        } else {
            siteManagementService.saveSite(match.getSite(), userContext);
        }

        matchDAO.saveMatch(match);
        log.info("Nouveau match enregistré avec l'ID {}", match.getId());

        registerPlayer(createur, match, null, userContext);
    }

    @Transactional
    @Override
    public void registerPlayer(Player player, Match match, Car car, UserContext userContext)
            throws ApplicationException, DatabaseException {
        if (player == null || player.getId() == null || match == null || match.getId() == null) {
            throw new ApplicationException("Invalid arguments to join a match");
        }

        if (match.getNumPlayersMax() != null && match.getNumPlayersMax() > match.getNumRegisteredPlayers()) {
            throw new ApplicationException("Player already registered in match");
        }

        if (car != null) {
            processCarToJoinMatch(car, userContext);
        }

        if (!match.isPlayerRegistered(player)) {
            matchDAO.registerPlayer(player, match, car);
            log.info("Player registered to match");
        } else {
            throw new ApplicationException("Player already registered in match");
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

    /**
     * Loads or saves the car used to join a match. If a car ID is detected but not found, a new car registration is
     * made by using the car passed in parameters
     *
     * @param car The car used by the registration
     * @param userContext
     * @throws DatabaseException
     */
    private void processCarToJoinMatch(Car car, UserContext userContext) throws DatabaseException {
        Car existingCar = null;

        if (car.getId() != null) {
            existingCar = carDAO.findCarById(car.getId());
        }

        if (existingCar == null) {
            // Save the new car
            carManagementService.saveCar(car, userContext);
        }
    }

    /**
     *
     * @return
     */
    private String generateMatchCode() {
        log.info("Generating new match code");
        return codeGenerator.generate(CODE_LENGTH);
    }
}
