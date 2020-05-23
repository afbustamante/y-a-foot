package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.model.UserContext;

/**
 * Site management service
 */
public interface SiteManagementService {

    /**
     * Registers a new site
     *
     * @param site Site to register
     * @param userContext User context for the responsible of this action
     * @return New site's identifier
     * @throws DatabaseException
     */
    Integer saveSite(Site site, UserContext userContext) throws DatabaseException;
}
