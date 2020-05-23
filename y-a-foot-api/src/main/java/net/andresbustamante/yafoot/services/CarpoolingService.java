package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;

/**
 * Carpooling services
 */
public interface CarpoolingService {

    /**
     * Update the car used for a registration. If the user performing the action is the owner of the car, the car is
     * confirmed for this match. Elsewhere, an exception is thrown.
     *
     * @param match Match to update
     * @param player Player to update
     * @param car Car to use for the registration
     * @param isCarConfirmed Indicates whether the user wants to accept and confirm a seat for carpool
     * @param ctx Context of the user performing this action
     * @throws DatabaseException
     * @throws ApplicationException If the context does not belong to the owner of the car
     */
    void updateCarpoolingInformation(Match match, Player player, Car car, boolean isCarConfirmed, UserContext ctx)
            throws DatabaseException, ApplicationException;

    /**
     * If the carpooling feature is enabled for the input match, it sends an email message asking for a place to
     * the driver of the input car for this match
     *
     * @param match The match selected by the player
     * @param player Player asking for a seat
     * @param car Car selected by the player
     * @param ctx Context of the user performing this action
     * @throws DatabaseException
     * @throws ApplicationException
     */
    void processCarSeatRequest(Match match, Player player, Car car, UserContext ctx) throws DatabaseException,
            ApplicationException;
}
