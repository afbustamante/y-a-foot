package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andresbustamante
 */
@Service
public class PlayerManagementServiceImpl implements PlayerManagementService {

    @Autowired
    private JoueurDAO joueurDAO;

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private VoitureDAO voitureDAO;

    private final Logger log = LoggerFactory.getLogger(PlayerManagementService.class);

    @Transactional
    @Override
    public boolean savePlayer(Joueur joueur, Contexte contexte) throws LdapException, DatabaseException {
        if (!joueurDAO.isJoueurInscrit(joueur.getEmail())) {
            // Créer l'utilisateur sur l'annuaire LDAP
            utilisateurDAO.creerUtilisateur(joueur, RolesEnum.JOUEUR);
            // Créer le joueur en base de données
            joueurDAO.creerJoueur(joueur);
            log.info("Nouveau joueur enregistré avec l'address {}", joueur.getEmail());
            return true;
        } else {
            log.info("Rejet : Joueur existant avec l'address {}", joueur.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public boolean updatePlayer(Joueur joueur, Contexte contexte) throws LdapException, DatabaseException {
        Joueur joueurExistant = joueurDAO.chercherJoueurParEmail(joueur.getEmail());
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
            log.info("Joueur mis à jour avec l'address {}", joueur.getEmail());
            return true;
        } else {
            log.info("Rejet : Joueur inexistant avec l'address {}", joueur.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deactivatePlayer(String emailJoueur, Contexte contexte) throws LdapException, DatabaseException {
        Joueur joueur = joueurDAO.chercherJoueurParEmail(emailJoueur);

        if (joueur != null) {
            // Supprimer les données du joueur
            int nbMatchs = matchDAO.desinscrireJoueur(joueur);
            log.info("Joueur {} desinscrit de {} matchs", emailJoueur, nbMatchs);

            int nbVoitures = voitureDAO.supprimerVoitures(joueur);
            log.info("{} voitures supprimées pour le joueur {}", nbVoitures, emailJoueur);

            int nbJoueursDesactives = joueurDAO.desactiverJoueur(joueur);
            log.info("{} joueur désactivé", nbJoueursDesactives);

            // Supprimer l'entrée LDAP
            utilisateurDAO.supprimerUtilisateur(joueur, new RolesEnum[]{RolesEnum.JOUEUR});
            return true;
        } else {
            return false;
        }
    }
}
