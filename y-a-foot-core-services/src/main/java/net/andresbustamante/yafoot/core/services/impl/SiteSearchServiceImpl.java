package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.core.services.SiteSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
@Service
public class SiteSearchServiceImpl implements SiteSearchService {

    private final SiteDao siteDAO;
    private final PlayerDao playerDao;

    @Autowired
    public SiteSearchServiceImpl(final SiteDao siteDAO, final PlayerDao playerDao) {
        this.siteDAO = siteDAO;
        this.playerDao = playerDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Site> findSites(final UserContext ctx) {
        Player player = playerDao.findPlayerByEmail(ctx.getUsername());
        return player != null ? siteDAO.findSitesByPlayer(player) : Collections.emptyList();
    }
}
