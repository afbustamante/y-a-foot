package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Car;

/**
 * Service de gestion des matchs
 *
 * @author andresbustamante
 */
public interface MatchManagementService {

    /**
     * Create a new match in database
     *
     * @param match Match to create
     * @param userContext
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void saveMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException;

    /**
     * Register a player to an existing match
     *
     * @param player Player to register
     * @param match Match to search
     * @param car Car used by the player to assist to the match. If the player is registering himself but is not the
     *            owner of the car, the player is registered but the car seat is not confirmed until the owner confirms it
     * @param userContext
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void registerPlayer(Player player, Match match, Car car, UserContext userContext) throws ApplicationException, DatabaseException;

    /**
     * Unregister a player from a match
     *
     * @param player Player to unregister
     * @param match Match to search
     * @param userContext
     * @return
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void unregisterPlayer(Player player, Match match, UserContext userContext) throws DatabaseException, ApplicationException;

    /**
     * Unregister a player from all matches
     *
     * @param player Player to search
     * @param userContext User context
     * @throws DatabaseException
     */
    void unregisterPlayerFromAllMatches(Player player, UserContext userContext) throws DatabaseException;
}
