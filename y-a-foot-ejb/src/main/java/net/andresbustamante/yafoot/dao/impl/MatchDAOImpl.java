package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.model.Match;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author andresbustamante
 */
@Stateless
public class MatchDAOImpl extends JpaDAO<Match> implements MatchDAO {

    @PersistenceContext(unitName = "y-a-foot-ejb_PU")
    private EntityManager em;

    public MatchDAOImpl() {
        super(Match.class);
    }

    @Override
    public boolean isCodeExistant(String codeMatch) {
        try {
            Query consulta = getEntityManager().createNamedQuery("Match.findIdByCode");
            consulta.setParameter("codeMatch", codeMatch);
            Object id = consulta.getSingleResult();
            return (id != null);
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
