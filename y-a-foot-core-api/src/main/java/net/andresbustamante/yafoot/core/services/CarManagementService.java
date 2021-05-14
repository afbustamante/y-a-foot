package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.Car;

/**
 * Service for managing cars in the DB
 */
public interface CarManagementService {

    /**
     * Registers a new car in the database
     *
     * @param car New car to register
     * @param ctx User context
     * @return
     * @throws DatabaseException
     */
    Integer saveCar(Car car, UserContext ctx) throws DatabaseException;

    /**
     * Deletes all cars registered by a player
     *
     * @param player Player to look for
     * @param ctx User context for the responsible of this operation
     * @throws DatabaseException
     */
    void deleteCarsByPlayer(Player player, UserContext ctx) throws DatabaseException;
}
