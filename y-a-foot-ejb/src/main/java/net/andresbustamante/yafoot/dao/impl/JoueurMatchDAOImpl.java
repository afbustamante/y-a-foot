package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.JoueurMatchDAO;
import net.andresbustamante.yafoot.model.JoueurMatch;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author andresbustamante
 */
@Stateless
public class JoueurMatchDAOImpl extends JpaDAO<JoueurMatch> implements JoueurMatchDAO {

    @PersistenceContext(unitName = "y-a-foot-ejb_PU")
    private EntityManager em;

    public JoueurMatchDAOImpl() {
        super(JoueurMatch.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
