package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Site management service implementation.
 */
@Service
public class SiteManagementServiceImpl implements SiteManagementService {

    private final SiteDao siteDAO;
    private final PlayerDao playerDao;

    @Autowired
    public SiteManagementServiceImpl(SiteDao siteDAO, PlayerDao playerDao) {
        this.siteDAO = siteDAO;
        this.playerDao = playerDao;
    }

    @Transactional
    @Override
    public Integer saveSite(Site site, UserContext userContext) throws DatabaseException {
        Player creator = playerDao.findPlayerByEmail(userContext.getUsername());

        siteDAO.saveSite(site, creator);
        return site.getId();
    }
}
