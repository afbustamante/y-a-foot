package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @author andresbustamante
 */
@Service
public class GestionJoueursServiceImpl implements GestionJoueursService {

    @Autowired
    private JoueurDAO joueurDAO;

    private final Log log = LogFactory.getLog(GestionJoueursService.class);

    @Transactional
    @Override
    public boolean inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        try {
            if (!joueurDAO.isJoueurInscrit(joueur.getEmail())) {
                joueurDAO.creerJoueur(joueur);
                log.info("Nouveau joueur enregistré avec l'adresse " + joueur.getEmail());
                return true;
            } else {
                log.info("Rejet : Joueur existant avec l'adresse " + joueur.getEmail());
                return false;
            }
        } catch (SQLException | DataAccessException e) {
            log.error("Erreur lors de la création d'un utilisateur", e);
            throw new BDDException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean actualiserJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        try {
            Joueur joueurExistant = joueurDAO.chercherJoueurParId(joueur.getId());

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
                joueurDAO.actualiserJoueur(joueurExistant);
                log.info("Joueur mis à jour avec l'adresse " + joueur.getEmail());
                return true;
            } else {
                log.info("Rejet : Joueur inexistant avec l'adresse " + joueur.getEmail());
                return false;
            }
        } catch (SQLException | DataAccessException e) {
            log.error("Erreur lors de la mise à jour d'un utilisateur", e);
            throw new BDDException(e.getMessage());
        }
    }

    @Override
    public Joueur chercherJoueur(String email, Contexte contexte) throws BDDException {
        try {
            return joueurDAO.chercherJoueurParEmail(email);
        } catch (SQLException | DataAccessException e) {
            throw new BDDException(e.getMessage());
        }
    }
}
