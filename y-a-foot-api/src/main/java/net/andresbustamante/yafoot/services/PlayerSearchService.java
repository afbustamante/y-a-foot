package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;

/**
 * Player research service. Only used for reading needs
 *
 * @author andresbustamante
 */
public interface PlayerSearchService {

    /**
     * Look for a player by using its email address (unique)
     *
     * @param email Email address to search
     * @return Player details for this email
     * @throws DatabaseException
     */
    Player findPlayerByEmail(String email) throws DatabaseException;

    /**
     * Look for a player by using its technical identifier
     *
     * @param id Player's ID
     * @return Player details for this ID
     * @throws DatabaseException
     */
    Player findPlayerById(Integer id) throws DatabaseException;
}
