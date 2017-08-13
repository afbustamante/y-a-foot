package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.framework.exceptions.DatabaseException;
import net.andresbustamante.yafoot.dao.*;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.RandomStringGenerator;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author andresbustamante
 */
@Stateless
public class GestionMatchsServiceImpl implements GestionMatchsService {

    private static final Integer LONGUEUR_CODE = 10;

    private final RandomStringGenerator generateurCodes = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();

    private static final Log log = LogFactory.getLog(GestionMatchsService.class);

    @EJB
    private MatchDAO matchDAO;

    @EJB
    private SiteDAO siteDAO;

    @EJB
    private VoitureDAO voitureDAO;

    @EJB
    private JoueurMatchDAO joueurMatchDAO;

    @EJB
    private JoueurDAO joueurDAO;

    @Override
    public void creerMatch(Match match, Contexte contexte) throws BDDException {
        try {
            String codeMatch;
            boolean codeDejaUtilise;
            do {
                codeMatch = genererCodeMatch();
                codeDejaUtilise = matchDAO.isCodeExistant(codeMatch);
            } while (codeDejaUtilise);

            match.setCode(codeMatch);

            if (match.getSite().getId() != null) {
                Site siteExistant = siteDAO.load(match.getSite().getId());

                if (siteExistant != null) {
                    match.setSite(siteExistant);
                } else {
                    // Créer aussi le site
                    match.getSite().setId(null);
                    siteDAO.save(match.getSite());
                }
            } else {
                siteDAO.save(match.getSite());
            }

            matchDAO.save(match);
            log.info("Nouveau match enregistré avec l'ID " + match.getId());
        } catch (DatabaseException e) {
            throw new BDDException(e.getMessage());
        }
    }

    @Override
    public void inscrireJoueurMatch(Joueur joueur, Match match, Voiture voiture, Contexte contexte)
            throws BDDException {
        if (joueur == null || joueur.getId() == null || match == null || match.getId() == null) {
            return;
        }

        try {
            Voiture voitureExistante = null;

            if (voiture != null) {
                voitureExistante = voitureDAO.load(voiture.getId());
            }

            boolean isJoueurExistant = (joueurDAO.load(joueur.getId()) != null);
            boolean isMatchExistant = (matchDAO.load(match.getId()) != null);

            JoueurMatch joueurMatch = new JoueurMatch(match.getId(), joueur.getId());

            if (isJoueurExistant && isMatchExistant && (joueurMatchDAO.load(joueurMatch.getId()) == null) && (
                    (voiture == null) || (voitureExistante != null))) {
                if (voitureExistante != null) {
                    joueurMatch.setVoiture(voiture);
                }
                joueurMatchDAO.save(joueurMatch);
                log.info("Joueur inscrit au match");
            } else {
                throw new BDDException("Impossible d'inscrire le joueur : objet inexistant");
            }
        } catch (DatabaseException e) {
            throw new BDDException(e.getMessage());
        }
    }

    private String genererCodeMatch() {
        return generateurCodes.generate(LONGUEUR_CODE);
    }
}
