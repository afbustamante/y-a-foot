package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
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
     * @param userContext
     * @return
     * @throws DatabaseException
     */
    List<Voiture> findCarsByPlayer(Joueur player, UserContext userContext) throws DatabaseException;
}
