package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;

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
}
