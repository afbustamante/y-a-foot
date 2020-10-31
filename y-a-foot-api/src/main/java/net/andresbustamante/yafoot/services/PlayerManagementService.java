package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;

/**
 * Players management service
 *
 * @author andresbustamante
 */
public interface PlayerManagementService {

    /**
     * Creates a new player in the application
     *
     * @param player Player to create
     * @param userContext Request context
     * @return New player's identifier
     * @throws DatabaseException
     * @throws LdapException
     */
    Integer savePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException;

    /**
     * Updates basic details for a player
     *
     * @param player Player to update
     * @param userContext Request context
     * @throws DatabaseException
     * @throws LdapException
     * @throws ApplicationException
     */
    void updatePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException;

    /**
     * Deactivates a player and deletes his/her history in the application
     *
     * @param player Player to deactivate
     * @param userContext Request context
     * @throws DatabaseException
     * @throws LdapException
     */
    void deactivatePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException;
}
