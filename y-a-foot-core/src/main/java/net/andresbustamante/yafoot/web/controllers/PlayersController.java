package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.exceptions.PlayerNotFoundException;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.web.mappers.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

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

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> createPlayer(Player player) {
        try {
            net.andresbustamante.yafoot.model.Player newPlayer = playerMapper.map(player);
            int id = playerManagementService.savePlayer(newPlayer, new net.andresbustamante.yafoot.model.UserContext());

            String location = MessageFormat.format(playerApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), new String[]{player.getEmail()}));
        } catch (DatabaseException | LdapException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> updatePlayer(Integer id, Player player) {
        try {
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.model.Player p = playerSearchService.findPlayerById(id);

            if (p != null) {
                playerManagementService.updatePlayer(playerMapper.map(player), userContext);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException | LdapException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(INVALID_USER_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Player> findPlayer(String email) {
        try {
            net.andresbustamante.yafoot.model.Player player = playerSearchService.findPlayerByEmail(email);

            if (player != null) {
                return ResponseEntity.ok(playerMapper.map(player));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> deactivatePlayer(Integer id) {
        try {
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.model.Player player = playerSearchService.findPlayerById(id);

            if (player != null) {
                playerManagementService.deactivatePlayer(player, userContext);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException | LdapException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }
}
