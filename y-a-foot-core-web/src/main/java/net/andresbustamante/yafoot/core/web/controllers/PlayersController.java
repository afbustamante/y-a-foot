package net.andresbustamante.yafoot.core.web.controllers;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.services.PlayerManagementService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.web.mappers.PlayerMapper;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.web.dto.PlayerForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * REST Controller to manage operations on players.
 *
 * @author andresbustamante
 */
@RestController
public class PlayersController extends AbstractController implements PlayersApi {

    private final PlayerManagementService playerManagementService;
    private final PlayerSearchService playerSearchService;
    private final PlayerMapper playerMapper;

    public PlayersController(
            PlayerManagementService playerManagementService, PlayerSearchService playerSearchService,
            PlayerMapper playerMapper) {
        this.playerManagementService = playerManagementService;
        this.playerSearchService = playerSearchService;
        this.playerMapper = playerMapper;
    }

    @Override
    public ResponseEntity<Player> loadPlayer(@Min(1) Integer id) {
        return ResponseEntity.status(NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<Void> updatePlayer(Integer id, PlayerForm player) {
        try {
            UserContext userContext = getUserContext();

            net.andresbustamante.yafoot.core.model.Player p = playerSearchService.findPlayerById(id);

            if (p != null) {
                net.andresbustamante.yafoot.core.model.Player mappedPlayer = playerMapper.map(player);
                mappedPlayer.setId(id);

                playerManagementService.updatePlayer(mappedPlayer, userContext);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException | DirectoryException e) {
            log.error("An error occurred while updating a player", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            log.error("Unable to update player after an application exception", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<Player>> findPlayers(String email) {
        try {
            UserContext ctx = getUserContext();

            net.andresbustamante.yafoot.core.model.Player player = playerSearchService.findPlayerByEmail(email, ctx);

            if (player != null) {
                return ResponseEntity.ok(playerMapper.map(List.of(player)));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (ApplicationException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> deactivatePlayer(Integer id) {
        try {
            UserContext userContext = getUserContext();

            net.andresbustamante.yafoot.core.model.Player player = playerSearchService.findPlayerById(id);

            if (player != null) {
                playerManagementService.deactivatePlayer(player, userContext);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException | DirectoryException e) {
            log.error("An error occurred while deactivating a player", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }
}
