package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.core.dao.MatchDAO;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.MatchSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
            OffsetDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime() : null;
            OffsetDateTime endDateTime = (endDate != null) ? endDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1L).toOffsetDateTime() : null;
            return matchDAO.findMatchesByPlayer(player, startDateTime, endDateTime);
        } else {
            return Collections.emptyList();
        }
    }
}
