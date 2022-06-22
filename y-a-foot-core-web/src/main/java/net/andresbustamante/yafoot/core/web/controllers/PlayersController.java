package net.andresbustamante.yafoot.core.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.services.PlayerManagementService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.core.web.mappers.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * REST Controller to manage operations on players
 *
 * @author andresbustamante
 */
@RestController
public class PlayersController extends AbstractController implements PlayersApi {

    private PlayerManagementService playerManagementService;

    private PlayerSearchService playerSearchService;

    private PlayerMapper playerMapper;

    @Value("${player.api.service.path}")
    private String playerApiPath;

    @Autowired
    public PlayersController(PlayerManagementService playerManagementService, PlayerSearchService playerSearchService,
                             PlayerMapper playerMapper, HttpServletRequest request, ObjectMapper objectMapper,
                             ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
        this.playerManagementService = playerManagementService;
        this.playerSearchService = playerSearchService;
        this.playerMapper = playerMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Player> loadPlayer(@Min(1) Integer playerId) {
        return ResponseEntity.status(NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<Void> createPlayer(Player player) {
        try {
            net.andresbustamante.yafoot.core.model.Player newPlayer = playerMapper.map(player);
            int id = playerManagementService.savePlayer(newPlayer, new UserContext());

            String location = MessageFormat.format(playerApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), new String[]{player.getEmail()}));
        } catch (DatabaseException | DirectoryException e) {
            log.error("An error occurred while creating a player", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> updatePlayer(Integer id, Player player) {
        try {
            UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.core.model.Player p = playerSearchService.findPlayerById(id);

            if (p != null) {
                playerManagementService.updatePlayer(playerMapper.map(player), userContext);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException | DirectoryException e) {
            log.error("An error occurred while updating a player", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<Player>> findPlayers(String email) {
        try {
            net.andresbustamante.yafoot.core.model.Player player = playerSearchService.findPlayerByEmail(email);

            if (player != null) {
                return ResponseEntity.ok(playerMapper.map(List.of(player)));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> deactivatePlayer(Integer id) {
        try {
            UserContext userContext = getUserContext(request);

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
