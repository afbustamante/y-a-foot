package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Match;

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
     * @param userContext
     * @return
     * @throws DatabaseException
     */
    Match findMatchByCode(String codeMatch, UserContext userContext) throws DatabaseException;

    /**
     * Chercher les matchs à venir pour un joueur dont l'identifiant est passé en paramètre
     *
     * @param idJoueur Identifiant du joueur
     * @param userContext
     * @return
     * @throws DatabaseException
     */
    List<Match> findMatchesByPlayer(Integer idJoueur, UserContext userContext) throws DatabaseException;
}
