package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.Car;

/**
 * Service for managing cars in the DB.
 */
public interface CarManagementService {

    /**
     * Registers a new car in the database.
     *
     * @param car New car to register
     * @param ctx User context
     * @return New car ID
     * @throws DatabaseException
     */
    Integer saveCar(Car car, UserContext ctx) throws DatabaseException;

    /**
     * Updates the car with the given ID.
     *
     * @param carId Car ID to search
     * @param updatedCar Car details to update
     * @param ctx User context
     * @throws ApplicationException If the current user is not allowed to perform this action
     * @throws DatabaseException
     */
    void updateCar(Integer carId, Car updatedCar, UserContext ctx) throws ApplicationException, DatabaseException;

    /**
     * Deactivates a given car.
     *
     * @param car Car to deactivate
     * @param ctx User context
     * @throws ApplicationException If the current user is not allowed to perform this action of if the car is being
     * used for a future match
     * @throws DatabaseException
     */
    void deactivateCar(Car car, UserContext ctx) throws ApplicationException, DatabaseException;

    /**
     * Deactivates all the cars registered by a player.
     *
     * @param player Player to look for
     * @param ctx User context for the responsible of this operation
     * @throws DatabaseException
     */
    void deactivateCarsByPlayer(Player player, UserContext ctx) throws DatabaseException;
}
