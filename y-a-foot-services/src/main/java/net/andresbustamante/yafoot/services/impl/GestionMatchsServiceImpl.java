package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @author andresbustamante
 */
@Service
public class GestionMatchsServiceImpl implements GestionMatchsService {

    private static final Integer NOUVEL_ID = -1;
    private static final Integer LONGUEUR_CODE = 10;

    private final RandomStringGenerator generateurCodes = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private static final Logger log = LoggerFactory.getLogger(GestionMatchsService.class);

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private SiteDAO siteDAO;

    @Autowired
    private VoitureDAO voitureDAO;

    @Autowired
    private JoueurDAO joueurDAO;

    @Transactional
    @Override
    public boolean creerMatch(Match match, Contexte contexte) throws BDDException {
        try {
            String codeMatch;
            boolean codeDejaUtilise;
            do {
                codeMatch = genererCodeMatch();
                codeDejaUtilise = matchDAO.isCodeExistant(codeMatch);
            } while (codeDejaUtilise);

            match.setCode(codeMatch);

            Joueur createur = joueurDAO.chercherJoueurParId(contexte.getIdUtilisateur());

            if (createur != null) {
                match.setCreateur(createur);
            } else {
                throw new BDDException("Identifiant d'utilisateur non trouvé en BDD : " + contexte.getIdUtilisateur());
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

            matchDAO.creerMatch(match);
            log.info("Nouveau match enregistré avec l'ID " + match.getId());

            inscrireJoueurMatch(createur, match, null, contexte);
            return true;
        } catch (SQLException | DataAccessException e) {
            log.error("Erreur lors de la création d'un match", e);
            throw new BDDException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean inscrireJoueurMatch(Joueur joueur, Match match, Voiture voiture, Contexte contexte)
            throws BDDException {
        if (joueur == null || joueur.getId() == null || match == null || match.getId() == null) {
            return false;
        }

        try {
            Voiture voitureExistante = null;

            if (voiture != null) {
                if (voiture.getId() != null) {
                    voitureExistante = voitureDAO.chercherVoitureParId(voiture.getId());
                }

                if (voitureExistante == null) {
                    // Enregistrer la voiture en base
                    voitureDAO.enregistrerVoiture(voiture);
                }
            }

            boolean isJoueurExistant = (joueurDAO.chercherJoueurParId(joueur.getId()) != null);
            boolean isMatchExistant = (matchDAO.chercherMatchParId(match.getId()) != null);

            if (isJoueurExistant && isMatchExistant && (!matchDAO.isJoueurInscritMatch(joueur, match))) {
                matchDAO.inscrireJoueurMatch(joueur, match, voiture);
                log.info("Joueur inscrit au match");
                return true;
            } else {
                throw new BDDException("Impossible d'inscrire le joueur : objet inexistant");
            }
        } catch (SQLException e) {
            log.error("Erreur de BDD lors de l'inscription d'un joueur à un match", e);
            throw new BDDException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean desinscrireJoueurMatch(Joueur joueur, Match match, Contexte contexte) throws BDDException {
        if (joueur == null || joueur.getId() == null || match == null || match.getCode() == null) {
            return false;
        }

        try {
            boolean isJoueurExistant = (joueurDAO.chercherJoueurParId(joueur.getId()) != null);
            boolean isMatchExistant = (matchDAO.chercherMatchParCode(match.getCode()) != null);

            if (isJoueurExistant && isMatchExistant && matchDAO.isJoueurInscritMatch(joueur, match)) {
                matchDAO.desinscrireJoueurMatch(joueur, match);
                log.info("Joueur désinscrit du match");
                return true;
            } else {
                throw new BDDException("Impossible d'inscrire le joueur : objet inexistant");
            }
        } catch (SQLException e) {
            log.error("Erreur de BDD lors de la désinscription d'un joueur", e);
            throw new BDDException(e.getMessage());
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
