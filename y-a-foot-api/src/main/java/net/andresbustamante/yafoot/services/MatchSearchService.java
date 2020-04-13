package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Match;

import java.time.LocalDate;
import java.util.List;

/**
 * Service de recherche des matchs
 *
 * @author andresbustamante
 */
public interface MatchSearchService {

    /**
     * Chercher un match par son code unique
     *
     * @param codeMatch Code du match à chercher
     * @return
     * @throws DatabaseException
     */
    Match findMatchByCode(String codeMatch) throws DatabaseException;

    /**
     * Chercher les matchs à venir pour un joueur dont l'identifiant est passé en paramètre
     *
     * @param player Player to use for the research
     * @param startDate Start date for the research
     * @param endDate End date for the research
     * @return
     * @throws DatabaseException
     */
    List<Match> findMatchesByPlayer(Player player, LocalDate startDate, LocalDate endDate) throws DatabaseException;
}
