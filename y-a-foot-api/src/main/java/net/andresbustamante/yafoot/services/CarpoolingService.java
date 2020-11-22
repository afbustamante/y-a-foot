package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;

import java.util.List;

/**
 * Carpooling services
 */
public interface CarpoolingService {

    /**
     * Finds the list of cars available to carpool for a given match
     *
     * @param match Match to search
     * @return The list of cars registered for the match
     * @throws DatabaseException
     */
    List<Car> findAvailableCarsByMatch(Match match) throws DatabaseException;

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

    /**
     * If a player changes of transportation it updates the carpooling options for passengers previously confirmed.
     *
     * If a new car is chosen to assist to a match and it belongs to the same owner, it transfers the passengers
     * already confirmed for the old car to the new one. If there is not enough seats, an exception is thrown to prevent
     * the user from this problem.
     *
     * If a new car is chosen but it belongs to another player or no new car is chosen, it resets the existing
     * confirmations and removes the car for passengers' registrations.
     *
     * @param match The match to process
     * @param oldCar Car used for the existing registration
     * @param newCar Car used for the transportation update. It can belong to another player.
     * @param ctx Context of the user performing this action
     * @throws ApplicationException If a transfer of passengers is not possible from the old car to the new one
     */
    void processTransportationChange(Match match, Car oldCar, Car newCar, UserContext ctx) throws ApplicationException;
}
