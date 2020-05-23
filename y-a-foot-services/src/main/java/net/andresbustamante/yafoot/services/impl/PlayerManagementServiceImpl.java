package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import net.andresbustamante.yafoot.services.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * @author andresbustamante
 */
@Service
public class PlayerManagementServiceImpl implements PlayerManagementService {

    private PlayerDAO playerDAO;

    private UserManagementService userManagementService;

    private MatchManagementService matchManagementService;

    private CarManagementService carManagementService;

    private final Logger log = LoggerFactory.getLogger(PlayerManagementServiceImpl.class);

    @Autowired
    public PlayerManagementServiceImpl(PlayerDAO playerDAO, UserManagementService userManagementService,
                                       MatchManagementService matchManagementService,
                                       CarManagementService carManagementService) {
        this.playerDAO = playerDAO;
        this.userManagementService = userManagementService;
        this.matchManagementService = matchManagementService;
        this.carManagementService = carManagementService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Integer savePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException {
        if (!playerDAO.isPlayerAlreadySignedUp(player.getEmail())) {
            // Créer l'utilisateur sur l'annuaire LDAP
            userManagementService.createUser(player, RolesEnum.PLAYER, userContext);
            // Create the player in database
            player.setCreationDate(ZonedDateTime.now());
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
    public boolean updatePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException {
        Player existingPlayer = playerDAO.findPlayerByEmail(player.getEmail());
        boolean needsDirectoryUpdate = false;

        if (existingPlayer != null) {
            existingPlayer.setLastUpdateDate(ZonedDateTime.now());

            if (player.getFirstName() != null) {
                existingPlayer.setFirstName(player.getFirstName());
                needsDirectoryUpdate = true;
            }
            if (player.getSurname() != null) {
                existingPlayer.setSurname(player.getSurname());
                needsDirectoryUpdate = true;
            }
            if (player.getPhoneNumber() != null) {
                existingPlayer.setPhoneNumber(player.getPhoneNumber());
            }

            if (needsDirectoryUpdate) {
                userManagementService.updateUser(player, userContext);
            }
            playerDAO.updatePlayer(existingPlayer);
            log.info("Player {} updated", player.getEmail());
            return true;
        } else {
            log.info("No user registered with the address {}", player.getEmail());
            return false;
        }
    }

    @Transactional
    @Override
    public void deactivatePlayer(Integer playerId, UserContext userContext) throws LdapException, DatabaseException {
        Player player = playerDAO.findPlayerById(playerId);

        if (player != null) {
            // Delete all data from player
            matchManagementService.unregisterPlayerFromAllMatches(player, userContext);
            carManagementService.deleteCarsByPlayer(player, userContext);

            int numPlayers = playerDAO.deactivatePlayer(player);
            log.info("Players deactivated: {}", numPlayers);

            player.setLastUpdateDate(ZonedDateTime.now());

            userManagementService.deleteUser(player, userContext);
        }
    }
}
