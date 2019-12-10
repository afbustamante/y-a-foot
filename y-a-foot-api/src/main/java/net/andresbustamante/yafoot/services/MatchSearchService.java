package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
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
     * @param contexte
     * @return
     * @throws DatabaseException
     */
    Match findMatchByCode(String codeMatch, Contexte contexte) throws DatabaseException;

    /**
     * Chercher les matchs à venir pour un joueur dont l'identifiant est passé en paramètre
     *
     * @param idJoueur Identifiant du joueur
     * @param contexte
     * @return
     * @throws DatabaseException
     */
    List<Match> findMatchesByPlayer(Integer idJoueur, Contexte contexte) throws DatabaseException;
}
