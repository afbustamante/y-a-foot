package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.services.MatchSearchService;
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
public class MatchSearchServiceImpl implements MatchSearchService {

    @Autowired
    private MatchDAO matchDAO;

    @Override
    public Match findMatchByCode(String codeMatch, UserContext userContext) throws DatabaseException {
        if (codeMatch != null) {
            return matchDAO.findMatchByCode(codeMatch);
        } else {
            return null;
        }
    }

    @Override
    public List<Match> findMatchesByPlayer(Integer idJoueur, UserContext userContext) throws DatabaseException {
        if (idJoueur != null && idJoueur > 0) {
            // Chercher les matchs programmés pour le joueur depuis 1 an
            LocalDateTime dateLocale = LocalDate.now(userContext.getTimezone()).atStartOfDay().minusYears(1L);
            ZonedDateTime date = ZonedDateTime.of(dateLocale, userContext.getTimezone());
            return matchDAO.findMatchesByPlayer(idJoueur, date);
        } else {
            return Collections.emptyList();
        }
    }
}