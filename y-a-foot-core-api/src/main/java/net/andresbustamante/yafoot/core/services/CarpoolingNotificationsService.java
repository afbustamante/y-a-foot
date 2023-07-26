package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * Service in charge of the notification for carpooling requests and responses.
 */
public interface CarpoolingNotificationsService {

    /**
     * Sends a notification to the driver of the car selected by a player for a carpooling request.
     *
     * @param playerId ID of the player asking for a seat
     * @param matchId Concerned match ID
     * @param carId ID of the selected car
     */
    void notifyCarpoolingRequest(Integer playerId, Integer matchId, Integer carId) throws ApplicationException;

    /**
     * Sends a notification to the player that asked for the seat on the given car with the answer from its driver.
     *
     * @param playerId ID of the player asking for a seat
     * @param matchId Concerned match ID
     * @param carId ID of the selected car
     * @param isCarSeatConfirmed Whether the seat has been confirmed (carpooling request)
     */
    void notifyCarpoolingUpdate(Integer playerId, Integer matchId, Integer carId, boolean isCarSeatConfirmed)
            throws ApplicationException;
}
