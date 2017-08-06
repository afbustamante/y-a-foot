package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.model.Site;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author andresbustamante
 */
@Stateless
public class SiteDAOImpl extends JpaDAO<Site> implements SiteDAO {

    @PersistenceContext(unitName = "y-a-foot-ejb_PU")
    private EntityManager em;

    public SiteDAOImpl() {
        super(Site.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
