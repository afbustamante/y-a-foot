package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.Site;

import java.util.List;

/**
 * Service useful for research operations.
 *
 * @author andresbustamante
 */
public interface SiteSearchService {

    /**
     * Load the sites associated to or registered by a player.
     *
     * @param ctx User context
     * @return List of sites found
     * @throws DatabaseException
     */
    List<Site> findSites(UserContext ctx) throws DatabaseException;
}
