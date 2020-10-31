package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Player;
import org.apache.ibatis.annotations.Param;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface describing the operations allowed on players in the database
 *
 * @author andresbustamante
 */
public interface PlayerDAO {

    /**
     * Looks for a player in the database using a technical identifier
     *
     * @param playerId Identifier to search
     * @return Player identified by this ID. Null if no player is found.
     */
    Player findPlayerById(@Param(ID) Integer playerId);

    /**
     * Checks if an email address is already registered for any player
     *
     * @param email Email address to search
     * @return True if the address is already registered
     */
    boolean isPlayerAlreadySignedUp(@Param(EMAIL) String email);

    /**
     * Creates a player in the database
     *
     * @param player Player to create
     */
    int savePlayer(@Param(PLAYER) Player player);

    /**
     * Updates player's details in database
     *
     * @param player Player to update
     */
    int updatePlayer(@Param(PLAYER) Player player);

    /**
     * Loads a player by using his/her email address
     *
     * @param email Email address to search
     * @return Player using the email address used as parameter. Null if no player is found for that address
     */
    Player findPlayerByEmail(@Param(EMAIL) String email);

    /**
     * Physically deletes a player from the database
     *
     * @param player Player to delete
     */
    int deletePlayer(@Param(PLAYER) Player player);


    /**
     * Logical deactivation for a player in database. This method will anonymize user's personal information when called
     *
     * @param player Player to deactivate
     */
    int deactivatePlayer(@Param(PLAYER) Player player);
}
