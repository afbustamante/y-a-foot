package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.model.Voiture;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author andresbustamante
 */
@Stateless
public class VoitureDAOImpl extends JpaDAO<Voiture> implements VoitureDAO {

    @PersistenceContext(unitName = "y-a-foot-ejb_PU")
    private EntityManager em;

    public VoitureDAOImpl() {
        super(Voiture.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
