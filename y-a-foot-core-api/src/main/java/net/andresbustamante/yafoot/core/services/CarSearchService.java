package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;

import java.util.List;

/**
 * Service useful for research operations
 */
public interface CarSearchService {

    /**
     * Load the list of cars saved by a user
     *
     * @param player Player to filter by
     * @return
     * @throws DatabaseException
     */
    List<Car> findCarsByPlayer(Player player) throws DatabaseException;

    /**
     * Loads the details of a car from its unique identifier
     *
     * @param id Car's identifier
     * @param ctx Context information for the actual user
     * @return Car details
     * @throws DatabaseException
     * @throws ApplicationException If the user is not the owner of the car. This operation is only allowed to the car's
     *                              owner
     */
    Car loadCar(Integer id, UserContext ctx) throws DatabaseException, ApplicationException;
}
