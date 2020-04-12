package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;

/**
 * Service de gestion des matchs
 *
 * @author andresbustamante
 */
public interface MatchManagementService {

    /**
     * Créer un match dans le système
     *
     * @param match Match à créer
     * @param userContext
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void saveMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException;

    /**
     * Inscrire un player à un match existant
     *
     * @param player Player à inscrire dans le match
     * @param match Match à impacter avec cette inscription
     * @param voiture Voiture dans laquelle le player va se déplacer vers le site de jeu
     * @param userContext
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void joinMatch(Player player, Match match, Voiture voiture, UserContext userContext) throws ApplicationException, DatabaseException;

    /**
     * Désinscrire un player d'un match
     *
     * @param player Player à désinscrire
     * @param match Match concerné
     * @param userContext
     * @return
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void quitMatch(Player player, Match match, UserContext userContext) throws DatabaseException, ApplicationException;

    /**
     * Unregister a player from all matches
     *
     * @param player Player to search
     * @param userContext User context
     * @throws DatabaseException
     */
    void quitAllMatches(Player player, UserContext userContext) throws DatabaseException;
}
