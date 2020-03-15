package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
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
    public Integer savePlayer(Joueur joueur, UserContext userContext) throws LdapException, DatabaseException, ApplicationException {
        if (!playerDAO.isPlayerAlreadySignedIn(joueur.getEmail())) {
            // Créer l'utilisateur sur l'annuaire LDAP
            userDAO.saveUser(joueur, RolesEnum.PLAYER);
            // Créer le joueur en base de données
            playerDAO.savePlayer(joueur);
            log.info("New player registered with the address {}", joueur.getEmail());
            return joueur.getId();
        } else {
            log.info("Existing player with the address {}", joueur.getEmail());
            throw new ApplicationException("A player already exists for this email address");
        }
    }

    @Transactional
    @Override
    public boolean updatePlayer(Joueur joueur, UserContext userContext) throws LdapException, DatabaseException {
        Joueur joueurExistant = playerDAO.findPlayerByEmail(joueur.getEmail());
        boolean isImpactLdap = false;

        if (joueurExistant != null) {
            if (joueur.getFirstName() != null) {
                joueurExistant.setFirstName(joueur.getFirstName());
                isImpactLdap = true;
            }
            if (joueur.getSurname() != null) {
                joueurExistant.setSurname(joueur.getSurname());
                isImpactLdap = true;
            }
            if (joueur.getPhoneNumber() != null) {
                joueurExistant.setPhoneNumber(joueur.getPhoneNumber());
            }
            if (joueur.getPassword() != null) {
                joueurExistant.setPassword(joueur.getPassword());
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
    public void deactivatePlayer(Integer playerId, UserContext userContext) throws LdapException, DatabaseException {
        Joueur joueur = playerDAO.findPlayerById(playerId);

        if (joueur != null) {
            // Supprimer les données du joueur
            int numMatches = matchDAO.unregisterPlayerFromAllMatches(joueur);
            log.info("Player unregistered from {} matches", numMatches);

            int numCars = carDAO.deleteCarsForPlayer(joueur);
            log.info("{} cars removed for player", numCars);

            int numPlayers = playerDAO.deactivatePlayer(joueur);
            log.info("Players deactivated: {}", numPlayers);

            // Supprimer l'entrée LDAP
            userDAO.deleteUser(joueur, new RolesEnum[]{RolesEnum.PLAYER});
        }
    }
}
