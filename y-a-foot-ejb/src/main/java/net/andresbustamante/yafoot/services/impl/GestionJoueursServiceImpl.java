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
    public void inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        try {
            if (!joueurDAO.isJoueurInscrit(joueur.getEmail())) {
                joueurDAO.save(joueur);
                log.info("Nouveau joueur enregistré avec l'adresse " + joueur.getEmail());
            } else {
                log.info("Rejet : Joueur existant avec l'adresse " + joueur.getEmail());
            }
        } catch (DatabaseException e) {
            throw new BDDException(e.getMessage());
        }
    }

    @Override
    public void actualiserJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        try {
            Joueur joueurExistant = joueurDAO.load(joueur.getId());

            if (joueurExistant != null) {
                if (joueur.getPrenom() != null) {
                    joueurExistant.setPrenom(joueur.getPrenom());
                }
                if (joueur.getNom() != null) {
                    joueurExistant.setNom(joueur.getNom());
                }
                if (joueur.getTelephone() != null) {
                    joueurExistant.setTelephone(joueur.getTelephone());
                }
                if (joueur.getMotDePasse() != null) {
                    joueurExistant.setMotDePasse(joueur.getMotDePasse());
                }
                joueurDAO.update(joueurExistant);
                log.info("Joueur mis à jour avec l'adresse " + joueur.getEmail());
            } else {
                log.info("Rejet : Joueur inexistant avec l'adresse " + joueur.getEmail());
            }
        } catch (DatabaseException e) {
            throw new BDDException(e.getMessage());
        }
    }

    @Override
    public Joueur chercherJoueur(String email, Contexte contexte) throws BDDException {
        try {
            return joueurDAO.chercherParMail(email);
        } catch (DatabaseException e) {
            throw new BDDException(e.getMessage());
        }
    }
}
