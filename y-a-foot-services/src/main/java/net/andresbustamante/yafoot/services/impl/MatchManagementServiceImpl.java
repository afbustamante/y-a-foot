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

/**
 * @author andresbustamante
 */
@Service
public class MatchManagementServiceImpl implements MatchManagementService {

    private static final Integer NOUVEL_ID = -1;
    private static final Integer LONGUEUR_CODE = 10;

    private final RandomStringGenerator generateurCodes = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

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
            codeMatch = genererCodeMatch();
            codeDejaUtilise = matchDAO.isCodeAlreadyRegistered(codeMatch);
        } while (codeDejaUtilise);

        match.setCode(codeMatch);

        Player createur = playerDAO.findPlayerByEmail(userContext.getUsername());

        if (createur != null) {
            match.setCreateur(createur);
        } else {
            throw new DatabaseException("Identifiant d'utilisateur non trouvé en BDD : " + userContext.getUsername());
        }

        if (match.getSite().getId() != null && !match.getSite().getId().equals(NOUVEL_ID)) {
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

        joinMatch(createur, match, null, userContext);
    }

    @Transactional
    @Override
    public void joinMatch(Player player, Match match, Voiture voiture, UserContext userContext)
            throws ApplicationException, DatabaseException {
        if (player == null || player.getId() == null || match == null || match.getId() == null) {
            throw new ApplicationException("Invalid arguments to join a match");
        }

        Voiture voitureExistante = null;

        if (voiture != null) {
            if (voiture.getId() != null) {
                voitureExistante = carDAO.findCarById(voiture.getId());
            }

            if (voitureExistante == null) {
                // Enregistrer la voiture en base
                carManagementService.saveCar(voiture, userContext);
            }
        }

        boolean isJoueurExistant = (playerDAO.findPlayerById(player.getId()) != null);
        boolean isMatchExistant = (matchDAO.findMatchById(match.getId()) != null);

        if (isJoueurExistant && isMatchExistant && (!matchDAO.isPlayerRegistered(player, match))) {
            matchDAO.registerPlayer(player, match, voiture);
            matchDAO.notifyPlayerRegistry(match);
            log.info("Player inscrit au match");
        } else {
            throw new DatabaseException("Impossible d'inscrire le player : objet inexistant");
        }
    }

    @Transactional
    @Override
    public void quitMatch(Player player, Match match, UserContext userContext) throws DatabaseException, ApplicationException {
        if (player == null || player.getId() == null || match == null || match.getCode() == null) {
            throw new ApplicationException("Invalid arguments to quit a match");
        }

        boolean isJoueurExistant = (playerDAO.findPlayerById(player.getId()) != null);
        boolean isMatchExistant = (matchDAO.findMatchByCode(match.getCode()) != null);

        if (isJoueurExistant && isMatchExistant && matchDAO.isPlayerRegistered(player, match)) {
            matchDAO.unregisterPlayer(player, match);
            matchDAO.notifyPlayerLeft(match);
            log.info("Player désinscrit du match");
        } else {
            throw new DatabaseException("Impossible d'inscrire le player : objet inexistant");
        }
    }

    @Transactional
    @Override
    public void quitAllMatches(Player player, UserContext userContext) throws DatabaseException {
        int numMatches = matchDAO.unregisterPlayerFromAllMatches(player);
        log.info("Player unregistered from {} matches", numMatches);
    }

    /**
     *
     * @return
     */
    private String genererCodeMatch() {
        log.info("Génération d'un nouveau code de match");
        return generateurCodes.generate(LONGUEUR_CODE);
    }
}
