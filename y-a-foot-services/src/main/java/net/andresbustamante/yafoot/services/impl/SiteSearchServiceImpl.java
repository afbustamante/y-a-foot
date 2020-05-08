package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.SiteSearchService;
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

    private SiteDAO siteDAO;

    @Autowired
    public SiteSearchServiceImpl(SiteDAO siteDAO) {
        this.siteDAO = siteDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Site> findSitesByPlayer(Player player) throws DatabaseException {
        if (player == null) {
            return Collections.emptyList();
        }
        return siteDAO.findSitesByPlayer(player);
    }
}
