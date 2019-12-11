package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Site;

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
     * @param idJoueur
     * @param userContext
     * @return
     * @throws DatabaseException
     */
    List<Site> findSitesByPlayer(Integer idJoueur, UserContext userContext) throws DatabaseException;
}
