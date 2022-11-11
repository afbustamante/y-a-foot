package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.core.services.SiteSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author andresbustamante
 */
@Service
public class SiteSearchServiceImpl implements SiteSearchService {

    private final SiteDao siteDAO;

    @Autowired
    public SiteSearchServiceImpl(SiteDao siteDAO) {
        this.siteDAO = siteDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Site> findSitesByPlayer(Player player) throws DatabaseException {
        return siteDAO.findSitesByPlayer(player);
    }
}
