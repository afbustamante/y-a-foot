package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Joueur;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author andresbustamante
 */
@Stateless
public class JoueurDAOImpl extends JpaDAO<Joueur> implements JoueurDAO {

    @PersistenceContext(unitName = "y-a-foot-ejb_PU")
    private EntityManager em;

    public JoueurDAOImpl() {
        super(Joueur.class);
    }

    @Override
    public boolean isJoueurInscrit(String email) throws BDDException {
        try {
            Query consulta = getEntityManager().createNamedQuery("Joueur.findIdByEmail");
            consulta.setParameter("email", email);
            Object joueur = consulta.getSingleResult();
            return (joueur != null);
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
