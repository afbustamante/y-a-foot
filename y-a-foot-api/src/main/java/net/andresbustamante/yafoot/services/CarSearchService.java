package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Voiture;

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
    List<Voiture> findCarsByPlayer(Player player) throws DatabaseException;
}
