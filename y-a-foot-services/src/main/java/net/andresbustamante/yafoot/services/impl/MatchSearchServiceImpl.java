package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.services.MatchSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
@Service
public class MatchSearchServiceImpl implements MatchSearchService {

    private MatchDAO matchDAO;

    @Autowired
    public MatchSearchServiceImpl(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    @Transactional(readOnly = true)
    @Override
    public Match findMatchByCode(String code) throws DatabaseException {
        if (code != null) {
            return matchDAO.findMatchByCode(code);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Match> findMatchesByPlayer(Player player, LocalDate startDate, LocalDate endDate) throws DatabaseException {
        if (player != null && player.getId() > 0) {
            ZonedDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay(ZoneId.systemDefault()) : null;
            ZonedDateTime endDateTime = (endDate != null) ? endDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1L) : null;
            return matchDAO.findMatchesByPlayer(player, startDateTime, endDateTime);
        } else {
            return Collections.emptyList();
        }
    }
}
