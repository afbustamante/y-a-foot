package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;

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
     * @throws DirectoryException
     */
    Integer savePlayer(Player player, UserContext userContext)
            throws DirectoryException, DatabaseException, ApplicationException;

    /**
     * Updates basic details for a player
     *
     * @param player Player to update
     * @param userContext Request context
     * @throws DatabaseException
     * @throws DirectoryException
     * @throws ApplicationException
     */
    void updatePlayer(Player player, UserContext userContext)
            throws DirectoryException, DatabaseException, ApplicationException;

    /**
     * Deactivates a player and deletes his/her history in the application
     *
     * @param player Player to deactivate
     * @param userContext Request context
     * @throws DatabaseException
     * @throws DirectoryException
     */
    void deactivatePlayer(Player player, UserContext userContext) throws DirectoryException, DatabaseException;
}
