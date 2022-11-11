package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.commons.model.UserContext;

/**
 * Site management service.
 */
public interface SiteManagementService {

    /**
     * Registers a new site.
     *
     * @param site Site to register
     * @param userContext User context for the responsible of this action
     * @return New site's identifier
     * @throws DatabaseException
     */
    Integer saveSite(Site site, UserContext userContext) throws DatabaseException;
}
