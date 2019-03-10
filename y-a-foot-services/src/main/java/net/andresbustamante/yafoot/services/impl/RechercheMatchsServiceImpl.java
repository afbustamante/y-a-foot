package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
@Service
public class RechercheMatchsServiceImpl implements RechercheMatchsService {

    @Autowired
    private MatchDAO matchDAO;

    @Override
    public Match chercherMatchParCode(String codeMatch, Contexte contexte) throws BDDException {
        try {
            if (codeMatch != null) {
                return matchDAO.chercherMatchParCode(codeMatch);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new BDDException("Problème de base de données : " + e.getMessage());
        }
    }

    @Override
    public List<Match> chercherMatchsJoueur(Integer idJoueur, Contexte contexte) throws BDDException {
        try {
            if (idJoueur != null && idJoueur > 0) {
                // Chercher les matchs programmés pour le joueur depuis ce matin
                LocalDateTime dateLocale = LocalDate.now(contexte.getTimeZone()).atStartOfDay();
                ZonedDateTime date = ZonedDateTime.of(dateLocale, contexte.getTimeZone());
                return matchDAO.chercherMatchsParJoueur(idJoueur, date);
            } else {
                return Collections.emptyList();
            }
        } catch (SQLException e) {
            throw new BDDException("Problème de base de données : " + e.getMessage());
        }
    }
}
