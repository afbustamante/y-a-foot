package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Sport;

import java.util.List;

public interface SportDao {

    /**
     * Loads the list of sports available in the application.
     *
     * @return List of sports found
     */
    List<Sport> loadSports();
}
