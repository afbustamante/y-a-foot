package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.framework.exceptions.DatabaseException;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
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

    private String genererCodeMatch() {
        return generateurCodes.generate(LONGUEUR_CODE);
    }
}
