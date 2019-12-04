package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Match chercherMatchParCode(String codeMatch, Contexte contexte) throws DatabaseException {
        if (codeMatch != null) {
            return matchDAO.chercherMatchParCode(codeMatch);
        } else {
            return null;
        }
    }

    @Override
    public List<Match> chercherMatchsJoueur(Integer idJoueur, Contexte contexte) throws DatabaseException {
        if (idJoueur != null && idJoueur > 0) {
            // Chercher les matchs programmés pour le joueur depuis 1 an
            LocalDateTime dateLocale = LocalDate.now(contexte.getTimezone()).atStartOfDay().minusYears(1L);
            ZonedDateTime date = ZonedDateTime.of(dateLocale, contexte.getTimezone());
            return matchDAO.chercherMatchsParJoueur(idJoueur, date);
        } else {
            return Collections.emptyList();
        }
    }
}
