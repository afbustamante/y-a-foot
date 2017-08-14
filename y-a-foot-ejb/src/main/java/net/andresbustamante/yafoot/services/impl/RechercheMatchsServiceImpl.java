package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.framework.exceptions.DatabaseException;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import net.andresbustamante.yafoot.util.DateUtils;
import org.joda.time.DateTime;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author andresbustamante
 */
@Stateless
public class RechercheMatchsServiceImpl implements RechercheMatchsService {

    @EJB
    private MatchDAO matchDAO;

    @Override
    public Match chercherMatchParCode(String codeMatch, Contexte contexte) throws BDDException {
        try {
            if (codeMatch != null) {
                return matchDAO.chercherParCode(codeMatch);
            } else {
                return null;
            }
        } catch (DatabaseException e) {
            throw new BDDException("Problème de base de données : " + e.getMessage());
        }
    }

    @Override
    public List<Match> chercherMatchsJoueur(Integer idJoueur, Contexte contexte) throws BDDException {
        try {
            if (idJoueur != null && idJoueur > 0) {
                // Chercher les matchs programmés pour le joueur depuis ce matin
                Date date = DateUtils.premiereMinuteDuJour(DateTime.now().toDate());
                return matchDAO.chercherParJoueur(idJoueur, date);
            } else {
                return Collections.emptyList();
            }
        } catch (DatabaseException e) {
            throw new BDDException("Problème de base de données : " + e.getMessage());
        }
    }
}
