package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.framework.exceptions.DatabaseException;
import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author andresbustamante
 */
@Stateless
public class GestionJoueursServiceImpl implements GestionJoueursService {

    @EJB
    private JoueurDAO joueurDAO;

    private final Log log = LogFactory.getLog(GestionJoueursService.class);

    @Override
    public Joueur inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        try {
            if (!joueurDAO.isJoueurInscrit(joueur.getEmail())) {
                joueurDAO.save(joueur);
                log.info("Nouveau joueur enregistré avec l'adresse " + joueur.getEmail());
                return joueur;
            } else {
                log.info("Rejet : Joueur existant avec l'adresse " + joueur.getEmail());
                return null;
            }
        } catch (DatabaseException e) {
            throw new BDDException(e.getMessage());
        }
    }

    @Override
    public Joueur actualiserJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        return null;
    }
}
