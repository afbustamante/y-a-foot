package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Site;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
@Stateless
public class SiteDAOImpl extends JpaDAO<Site> implements SiteDAO {

    private static final String POURCENTAGE = "%";

    @PersistenceContext(unitName = "y-a-foot-ejb_PU")
    private EntityManager em;

    public SiteDAOImpl() {
        super(Site.class);
    }

    @Override
    public List<Site> chercherParNom(String nom) throws BDDException {
        try {
            Query q = getEntityManager().createQuery("SELECT sit FROM Site sit " +
                    "WHERE sit.nom LIKE :nom");
            String nomLike = POURCENTAGE + nom + POURCENTAGE;
            q.setParameter("nom", nomLike);
            List sites = q.getResultList();
            return (sites != null) ? (List<Site>) sites : null;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
