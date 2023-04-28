package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import net.andresbustamante.yafoot.core.services.MatchSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Match search service implementation.
 *
 * @author andresbustamante
 */
@Service
public class MatchSearchServiceImpl implements MatchSearchService {

    private final MatchDao matchDAO;
    private final PlayerDao playerDao;

    @Autowired
    public MatchSearchServiceImpl(MatchDao matchDAO, PlayerDao playerDao) {
        this.matchDAO = matchDAO;
        this.playerDao = playerDao;
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
    public List<Match> findMatches(MatchStatusEnum status, SportEnum sport, LocalDate startDate,
                                   LocalDate endDate, UserContext ctx) {
        Player player = playerDao.findPlayerByEmail(ctx.getUsername());

        if (player != null && player.getId() > 0) {
            LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
            LocalDateTime endDateTime = (endDate != null)
                    ? endDate.plusDays(1L).atStartOfDay().minusSeconds(1L)
                    : null;
            return matchDAO.findMatchesByPlayer(player, sport, status, startDateTime, endDateTime);
        } else {
            return Collections.emptyList();
        }
    }
}
