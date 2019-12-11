package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.MatchManagementService;
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
    private CarDAO carDAO;

    @Autowired
    private PlayerDAO playerDAO;

    @Transactional
    @Override
    public boolean saveMatch(Match match, UserContext userContext) throws DatabaseException {
        String codeMatch;
        boolean codeDejaUtilise;
        do {
            codeMatch = genererCodeMatch();
            codeDejaUtilise = matchDAO.isCodeAlreadyRegistered(codeMatch);
        } while (codeDejaUtilise);

        match.setCode(codeMatch);

        Joueur createur = playerDAO.findPlayerById(userContext.getUserId());

        if (createur != null) {
            match.setCreateur(createur);
        } else {
            throw new DatabaseException("Identifiant d'utilisateur non trouvé en BDD : " + userContext.getUserId());
        }

        if (match.getSite().getId() != null && !match.getSite().getId().equals(NOUVEL_ID)) {
            Site siteExistant = siteDAO.chercherSiteParId(match.getSite().getId());

            if (siteExistant != null) {
                match.setSite(siteExistant);
            } else {
                // Créer aussi le site
                siteDAO.creerSite(match.getSite());
            }
        } else {
            siteDAO.creerSite(match.getSite());
        }

        matchDAO.saveMatch(match);
        log.info("Nouveau match enregistré avec l'ID {}", match.getId());

        joinMatch(createur, match, null, userContext);
        return true;
    }

    @Transactional
    @Override
    public boolean joinMatch(Joueur joueur, Match match, Voiture voiture, UserContext userContext)
            throws DatabaseException {
        if (joueur == null || joueur.getId() == null || match == null || match.getId() == null) {
            return false;
        }

        Voiture voitureExistante = null;

        if (voiture != null) {
            if (voiture.getId() != null) {
                voitureExistante = carDAO.findCarById(voiture.getId());
            }

            if (voitureExistante == null) {
                // Enregistrer la voiture en base
                carDAO.saveCar(voiture, joueur);
            }
        }

        boolean isJoueurExistant = (playerDAO.findPlayerById(joueur.getId()) != null);
        boolean isMatchExistant = (matchDAO.findMatchById(match.getId()) != null);

        if (isJoueurExistant && isMatchExistant && (!matchDAO.isPlayerRegistered(joueur, match))) {
            matchDAO.registerPlayer(joueur, match, voiture);
            matchDAO.notifyPlayerRegistry(match);
            log.info("Joueur inscrit au match");
            return true;
        } else {
            throw new DatabaseException("Impossible d'inscrire le joueur : objet inexistant");
        }
    }

    @Transactional
    @Override
    public boolean quitMatch(Joueur joueur, Match match, UserContext userContext) throws DatabaseException {
        if (joueur == null || joueur.getId() == null || match == null || match.getCode() == null) {
            return false;
        }

        boolean isJoueurExistant = (playerDAO.findPlayerById(joueur.getId()) != null);
        boolean isMatchExistant = (matchDAO.findMatchByCode(match.getCode()) != null);

        if (isJoueurExistant && isMatchExistant && matchDAO.isPlayerRegistered(joueur, match)) {
            matchDAO.unregisterPlayer(joueur, match);
            matchDAO.notifyPlayerLeft(match);
            log.info("Joueur désinscrit du match");
            return true;
        } else {
            throw new DatabaseException("Impossible d'inscrire le joueur : objet inexistant");
        }
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
