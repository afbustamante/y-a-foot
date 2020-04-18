package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private MatchManagementService matchManagementService;

    @Autowired
    private CarManagementService carManagementService;

    private final Logger log = LoggerFactory.getLogger(PlayerManagementServiceImpl.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Integer savePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException {
        if (!playerDAO.isPlayerAlreadySignedUp(player.getEmail())) {
            // Créer l'utilisateur sur l'annuaire LDAP
            userDAO.saveUser(player, RolesEnum.PLAYER);
            // Créer le player en base de données
            playerDAO.savePlayer(player);
            log.info("New player registered with the address {}", player.getEmail());
            return player.getId();
        } else {
            log.info("Existing player with the address {}", player.getEmail());
            throw new ApplicationException("A player already exists for this email address");
        }
    }

    @Transactional
    @Override
    public boolean updatePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException {
        Player existingPlayer = playerDAO.findPlayerByEmail(player.getEmail());
        boolean isImpactLdap = false;

        if (existingPlayer != null) {
            if (player.getFirstName() != null) {
                existingPlayer.setFirstName(player.getFirstName());
                isImpactLdap = true;
            }
            if (player.getSurname() != null) {
                existingPlayer.setSurname(player.getSurname());
                isImpactLdap = true;
            }
            if (player.getPhoneNumber() != null) {
                existingPlayer.setPhoneNumber(player.getPhoneNumber());
            }
            if (player.getPassword() != null) {
                existingPlayer.setPassword(player.getPassword());
                isImpactLdap = true;
            }

            if (isImpactLdap) {
                userDAO.updateUser(player);
            }
            playerDAO.updatePlayer(existingPlayer);
            log.info("Player mis à jour avec l'address {}", player.getEmail());
            return true;
        } else {
            log.info("Rejet : Player inexistant avec l'address {}", player.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public void deactivatePlayer(Integer playerId, UserContext userContext) throws LdapException, DatabaseException {
        Player player = playerDAO.findPlayerById(playerId);

        if (player != null) {
            // Delete all data from player
            matchManagementService.quitAllMatches(player, userContext);
            carManagementService.deleteCarsByPlayer(player, userContext);

            int numPlayers = playerDAO.deactivatePlayer(player);
            log.info("Players deactivated: {}", numPlayers);

            // Supprimer l'entrée LDAP
            userDAO.deleteUser(player, new RolesEnum[]{RolesEnum.PLAYER});
        }
    }
}
