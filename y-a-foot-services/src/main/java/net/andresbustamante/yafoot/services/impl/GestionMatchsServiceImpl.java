package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.GestionMatchsService;
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
public class GestionMatchsServiceImpl implements GestionMatchsService {

    private static final Integer NOUVEL_ID = -1;
    private static final Integer LONGUEUR_CODE = 10;

    private final RandomStringGenerator generateurCodes = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private final Logger log = LoggerFactory.getLogger(GestionMatchsServiceImpl.class);

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
    public boolean creerMatch(Match match, Contexte contexte) throws DatabaseException {
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
            throw new DatabaseException("Identifiant d'utilisateur non trouvé en BDD : " + contexte.getIdUtilisateur());
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
        log.info("Nouveau match enregistré avec l'ID {}", match.getId());

        inscrireJoueurMatch(createur, match, null, contexte);
        return true;
    }

    @Transactional
    @Override
    public boolean inscrireJoueurMatch(Joueur joueur, Match match, Voiture voiture, Contexte contexte)
            throws DatabaseException {
        if (joueur == null || joueur.getId() == null || match == null || match.getId() == null) {
            return false;
        }

        Voiture voitureExistante = null;

        if (voiture != null) {
            if (voiture.getId() != null) {
                voitureExistante = voitureDAO.chercherVoitureParId(voiture.getId());
            }

            if (voitureExistante == null) {
                // Enregistrer la voiture en base
                voitureDAO.enregistrerVoiture(voiture, joueur);
            }
        }

        boolean isJoueurExistant = (joueurDAO.chercherJoueurParId(joueur.getId()) != null);
        boolean isMatchExistant = (matchDAO.chercherMatchParId(match.getId()) != null);

        if (isJoueurExistant && isMatchExistant && (!matchDAO.isJoueurInscritMatch(joueur, match))) {
            matchDAO.inscrireJoueurMatch(joueur, match, voiture);
            matchDAO.notifierInscriptionJoueur(match);
            log.info("Joueur inscrit au match");
            return true;
        } else {
            throw new DatabaseException("Impossible d'inscrire le joueur : objet inexistant");
        }
    }

    @Transactional
    @Override
    public boolean desinscrireJoueurMatch(Joueur joueur, Match match, Contexte contexte) throws DatabaseException {
        if (joueur == null || joueur.getId() == null || match == null || match.getCode() == null) {
            return false;
        }

        boolean isJoueurExistant = (joueurDAO.chercherJoueurParId(joueur.getId()) != null);
        boolean isMatchExistant = (matchDAO.chercherMatchParCode(match.getCode()) != null);

        if (isJoueurExistant && isMatchExistant && matchDAO.isJoueurInscritMatch(joueur, match)) {
            matchDAO.desinscrireJoueurMatch(joueur, match);
            matchDAO.notifierDesinscriptionJoueur(match);
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
