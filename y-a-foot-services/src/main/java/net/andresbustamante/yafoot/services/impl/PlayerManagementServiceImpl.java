package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.UserContext;
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
    private PlayerDAO playerDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private CarDAO carDAO;

    private final Logger log = LoggerFactory.getLogger(PlayerManagementServiceImpl.class);

    @Transactional
    @Override
    public boolean savePlayer(Joueur joueur, UserContext userContext) throws LdapException, DatabaseException {
        if (!playerDAO.isPlayerAlreadySignedIn(joueur.getEmail())) {
            // Créer l'utilisateur sur l'annuaire LDAP
            userDAO.saveUser(joueur, RolesEnum.JOUEUR);
            // Créer le joueur en base de données
            playerDAO.savePlayer(joueur);
            log.info("Nouveau joueur enregistré avec l'address {}", joueur.getEmail());
            return true;
        } else {
            log.info("Rejet : Joueur existant avec l'address {}", joueur.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public boolean updatePlayer(Joueur joueur, UserContext userContext) throws LdapException, DatabaseException {
        Joueur joueurExistant = playerDAO.findPlayerByEmail(joueur.getEmail());
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
                userDAO.updateUser(joueur);
            }
            playerDAO.updatePlayer(joueurExistant);
            log.info("Joueur mis à jour avec l'address {}", joueur.getEmail());
            return true;
        } else {
            log.info("Rejet : Joueur inexistant avec l'address {}", joueur.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deactivatePlayer(String emailJoueur, UserContext userContext) throws LdapException, DatabaseException {
        Joueur joueur = playerDAO.findPlayerByEmail(emailJoueur);

        if (joueur != null) {
            // Supprimer les données du joueur
            int nbMatchs = matchDAO.unregisterPlayerFromAllMatches(joueur);
            log.info("Joueur {} desinscrit de {} matchs", emailJoueur, nbMatchs);

            int nbVoitures = carDAO.deleteCarsForPlayer(joueur);
            log.info("{} voitures supprimées pour le joueur {}", nbVoitures, emailJoueur);

            int nbJoueursDesactives = playerDAO.deactivatePlayer(joueur);
            log.info("{} joueur désactivé", nbJoueursDesactives);

            // Supprimer l'entrée LDAP
            userDAO.deleteUser(joueur, new RolesEnum[]{RolesEnum.JOUEUR});
            return true;
        } else {
            return false;
        }
    }
}
