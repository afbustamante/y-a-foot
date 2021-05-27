package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Sport;

import java.util.List;

/**
 * Service useful for research operations
 *
 * @author andresbustamante
 */
public interface SportSearchService {

    /**
     * Finds all sports available in the application
     *
     * @return List of sports
     * @throws DatabaseException If there is a problem while requesting data from database
     */
    List<Sport> loadSports() throws DatabaseException;
}
