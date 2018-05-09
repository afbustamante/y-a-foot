package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.RandomStringGenerator;
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

    private static final Integer LONGUEUR_CODE = 10;

    private final RandomStringGenerator generateurCodes = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private static final Log log = LogFactory.getLog(GestionMatchsService.class);

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
            }

            if (match.getSite().getId() != null) {
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
                voitureExistante = voitureDAO.chercherVoitureParId(voiture.getId());
            }

            boolean isJoueurExistant = (joueurDAO.chercherJoueurParId(joueur.getId()) != null);
            boolean isMatchExistant = (matchDAO.chercherMatchParId(match.getId()) != null);

            if (isJoueurExistant && isMatchExistant && (!matchDAO.isJoueurInscritMatch(joueur, match)) && (
                    (voiture == null) || (voitureExistante != null))) {
                matchDAO.inscrireJoueurMatch(joueur, match, voiture);
                log.info("Joueur inscrit au match");
                return true;
            } else {
                throw new BDDException("Impossible d'inscrire le joueur : objet inexistant");
            }
        } catch (SQLException e) {
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
