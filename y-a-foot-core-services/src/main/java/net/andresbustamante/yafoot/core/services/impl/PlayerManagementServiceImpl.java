package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.exceptions.PlayerNotFoundException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.MatchManagementService;
import net.andresbustamante.yafoot.core.services.PlayerManagementService;
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

    private final PlayerDao playerDAO;
    private final UserManagementAdapter userManagementAdapter;
    private final MatchManagementService matchManagementService;
    private final CarManagementService carManagementService;

    private final Logger log = LoggerFactory.getLogger(PlayerManagementServiceImpl.class);

    @Autowired
    public PlayerManagementServiceImpl(final PlayerDao playerDAO, final UserManagementAdapter userManagementAdapter,
                                       final MatchManagementService matchManagementService,
                                       final CarManagementService carManagementService) {
        this.playerDAO = playerDAO;
        this.userManagementAdapter = userManagementAdapter;
        this.matchManagementService = matchManagementService;
        this.carManagementService = carManagementService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Integer savePlayer(final Player player, final UserContext userContext) throws ApplicationException {
        if (!playerDAO.isPlayerAlreadySignedUp(player.getEmail())) {
            // Create the player in database
            playerDAO.savePlayer(player);
            log.info("New player registered with the address {}", player.getEmail());
            return player.getId();
        } else {
            log.info("Existing player with the address {}", player.getEmail());
            throw new ApplicationException("email.already.registered.error",
                    "A player already exists for this email address");
        }
    }

    @Transactional
    @Override
    public void updatePlayer(final Player player, final UserContext userContext)
            throws DirectoryException, ApplicationException {
        Player existingPlayer = playerDAO.findPlayerById(player.getId());

        if (existingPlayer != null) {
            if (player.getFirstName() != null) {
                existingPlayer.setFirstName(player.getFirstName());
            }
            if (player.getSurname() != null) {
                existingPlayer.setSurname(player.getSurname());
            }
            if (player.getPhoneNumber() != null) {
                existingPlayer.setPhoneNumber(player.getPhoneNumber());
            }

            playerDAO.updatePlayer(existingPlayer);

            userManagementAdapter.updateUser(player, userContext);

            log.info("Player {} updated", player.getEmail());
        } else {
            log.info("No user registered with the address {}", player.getEmail());
            throw new PlayerNotFoundException("No player registered with the address " + player.getEmail());
        }
    }

    @Transactional
    @Override
    public void deactivatePlayer(final Player player, final UserContext userContext)
            throws DirectoryException, DatabaseException {
        // Delete all data from player
        matchManagementService.unregisterPlayerFromAllMatches(player, userContext);
        carManagementService.deactivateCarsByPlayer(player, userContext);

        int numPlayers = playerDAO.deactivatePlayer(player);
        log.info("Players deactivated: {}", numPlayers);

        userManagementAdapter.deleteUser(player, userContext);
    }
}
