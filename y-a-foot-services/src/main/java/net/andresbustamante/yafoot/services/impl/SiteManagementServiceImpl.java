package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.services.SiteManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Site management service implementation
 */
@Service
public class SiteManagementServiceImpl implements SiteManagementService {

    @Autowired
    private SiteDAO siteDAO;

    @Autowired
    private PlayerSearchService playerSearchService;

    @Transactional
    @Override
    public long saveSite(Site site, UserContext userContext) throws DatabaseException {
        Player creator = playerSearchService.findPlayerByEmail(userContext.getUsername());

        siteDAO.saveSite(site, creator);
        return site.getId();
    }
}
