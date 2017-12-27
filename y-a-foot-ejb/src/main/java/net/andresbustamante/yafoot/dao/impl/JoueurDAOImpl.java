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
            Query q = getEntityManager().createQuery("SELECT jou.id FROM Joueur jou WHERE jou.email = :email");
            q.setParameter("email", email);
            Object joueur = q.getSingleResult();
            return (joueur != null);
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Joueur chercherParMail(String email) throws BDDException {
        try {
            Query q = getEntityManager().createQuery("SELECT jou FROM Joueur jou WHERE jou.email = :email");
            q.setParameter("email", email);
            Object joueur = q.getSingleResult();
            return (Joueur) joueur;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
