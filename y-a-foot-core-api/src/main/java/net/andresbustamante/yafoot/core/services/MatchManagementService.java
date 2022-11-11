package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Car;

/**
 * Matches management service.
 *
 * @author andresbustamante
 */
public interface MatchManagementService {

    /**
     * Create a new match in database.
     *
     * @param match Match to create
     * @param userContext
     * @return New match ID
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    Integer saveMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException;

    /**
     * Register a player to an existing match. If the player is already registered, the existing registration is
     * updated by the new one.
     *
     * @param player Player to register
     * @param match Match to update
     * @param car Car used by the player to assist to the match. If the player is registering himself but is not the
     *            owner of the car, the player is registered but the car seat is not confirmed until the owner confirms
     *            it
     * @param userContext
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void registerPlayer(Player player, Match match, Car car, UserContext userContext)
            throws ApplicationException, DatabaseException;

    /**
     * Unregister a player from a match.
     *
     * @param player Player to unregister
     * @param match Match to search
     * @param userContext
     * @return
     * @throws DatabaseException
     * @throws ApplicationException Invalid arguments
     */
    void unregisterPlayer(Player player, Match match, UserContext userContext)
            throws DatabaseException, ApplicationException;

    /**
     * Unregister a player from all matches.
     *
     * @param player Player to search
     * @param userContext User context
     * @throws DatabaseException
     */
    void unregisterPlayerFromAllMatches(Player player, UserContext userContext) throws DatabaseException;

    /**
     * Cancels a match by a logical suppression in database. All registrations are kept for consultation.
     *
     * @param match Match to cancel
     * @param userContext Context of the user asking for this action
     * @throws DatabaseException Unexpected exception when updating the match in database
     * @throws ApplicationException When the match is already in the past or the user is not allowed to cancel the match
     */
    void cancelMatch(Match match, UserContext userContext) throws DatabaseException, ApplicationException;
}
