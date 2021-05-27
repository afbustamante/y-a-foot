package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * Matches search service. Only used for reading data
 *
 * @author andresbustamante
 */
public interface MatchSearchService {

    /**
     * Find a match by its identifying code
     *
     * @param code Code to search
     * @return Match found for this code
     * @throws DatabaseException
     */
    Match findMatchByCode(String code) throws DatabaseException;

    /**
     * Find the matches where a player is attending in an interval of time
     *
     * @param player Player to use for the research
     * @param status Match status
     * @param sport Sport to use for filtering research results
     * @param startDate Start date for the research
     * @param endDate End date for the research
     * @return
     * @throws DatabaseException
     */
    List<Match> findMatchesByPlayer(Player player, MatchStatusEnum status, SportEnum sport,
                                    LocalDate startDate, LocalDate endDate) throws DatabaseException;
}
