package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Site management service implementation
 */
@Service
public class SiteManagementServiceImpl implements SiteManagementService {

    private SiteDAO siteDAO;

    private PlayerSearchService playerSearchService;

    @Autowired
    public SiteManagementServiceImpl(SiteDAO siteDAO, PlayerSearchService playerSearchService) {
        this.siteDAO = siteDAO;
        this.playerSearchService = playerSearchService;
    }

    @Transactional
    @Override
    public Integer saveSite(Site site, UserContext userContext) throws DatabaseException {
        Player creator = playerSearchService.findPlayerByEmail(userContext.getUsername());

        siteDAO.saveSite(site, creator);
        return site.getId();
    }
}
