package net.andresbustamante.yafoot.dao.impl;

import net.andresbustamante.framework.dao.JpaDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Match;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

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
    public boolean isCodeExistant(String codeMatch) throws BDDException {
        try {
            Query q = getEntityManager().createQuery("SELECT mat.id FROM Match mat WHERE mat.code = :codeMatch");
            q.setParameter("codeMatch", codeMatch);
            Object id = q.getSingleResult();
            return (id != null);
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Match chercherParCode(String codeMatch) throws BDDException {
        try {
            Query q = getEntityManager().createQuery("SELECT mat FROM Match mat " +
                    "JOIN FETCH mat.site " +
                    "LEFT JOIN FETCH mat.joueursMatch " +
                    "WHERE mat.code = :codeMatch");
            q.setParameter("codeMatch", codeMatch);
            Object match = q.getSingleResult();
            return (match != null) ? (Match) match : null;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Match> chercherParJoueur(Integer idJoueur, Date dateAuPlusTot) throws BDDException {
        try {
            Query q = getEntityManager().createQuery("SELECT DISTINCT mat FROM Match mat " +
                    "JOIN FETCH mat.site " +
                    "JOIN FETCH mat.joueursMatch " +
                    "WHERE mat.id IN (SELECT DISTINCT jma.match.id FROM JoueurMatch jma WHERE jma.joueur.id = :idJoueur) " +
                    "  AND mat.dateMatch >= :dateAuPlusTot");
            q.setParameter("idJoueur", idJoueur);
            q.setParameter("dateAuPlusTot", dateAuPlusTot);
            List matchs = q.getResultList();
            return (matchs != null) ? (List<Match>) matchs : null;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
