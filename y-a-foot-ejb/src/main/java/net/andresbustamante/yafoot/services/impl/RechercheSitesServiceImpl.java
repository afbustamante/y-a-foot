package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.RechercheSitesService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author andresbustamante
 */
@Stateless
public class RechercheSitesServiceImpl implements RechercheSitesService {

    @EJB
    private SiteDAO siteDAO;

    @Override
    public List<Site> chercherSitesParNom(String nom, Contexte contexte) throws BDDException {
        return siteDAO.chercherParNom(nom);
    }
}
