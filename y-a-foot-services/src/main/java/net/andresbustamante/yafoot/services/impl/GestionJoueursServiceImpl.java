package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    private final Logger log = LoggerFactory.getLogger(GestionJoueursService.class);

    @Transactional
    @Override
    public boolean inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException {
        try {
            if (!joueurDAO.isJoueurInscrit(joueur.getEmail())) {
                // Créer l'utilisateur sur l'annuaire LDAP
                utilisateurDAO.creerUtilisateur(joueur, RolesEnum.JOUEUR);
                // Créer le joueur en base de données
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
            boolean isImpactLdap = false;

            if (joueurExistant != null) {
                if (joueur.getPrenom() != null) {
                    joueurExistant.setPrenom(joueur.getPrenom());
                    isImpactLdap = true;
                }
                if (joueur.getNom() != null) {
                    joueurExistant.setNom(joueur.getNom());
                    isImpactLdap = true;
                }
                if (joueur.getTelephone() != null) {
                    joueurExistant.setTelephone(joueur.getTelephone());
                }
                if (joueur.getMotDePasse() != null) {
                    joueurExistant.setMotDePasse(joueur.getMotDePasse());
                    isImpactLdap = true;
                }

                if (isImpactLdap) {
                    utilisateurDAO.actualiserUtilisateur(joueur);
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
}
