package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;

import java.util.List;

/**
 * Service useful for research operations
 *
 * @author andresbustamante
 */
public interface SiteSearchService {

    /**
     * Load the sites associated to or registered by a player
     *
     * @param player
     * @return
     * @throws DatabaseException
     */
    List<Site> findSitesByPlayer(Player player) throws DatabaseException;
}
