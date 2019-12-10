package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.SiteSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author andresbustamante
 */
@Service
public class SiteSearchServiceImpl implements SiteSearchService {

    @Autowired
    private SiteDAO siteDAO;

    @Override
    public List<Site> findSitesByPlayer(Integer idJoueur, Contexte contexte) throws DatabaseException {
        return siteDAO.chercherSitesPourJoueur(idJoueur);
    }
}
