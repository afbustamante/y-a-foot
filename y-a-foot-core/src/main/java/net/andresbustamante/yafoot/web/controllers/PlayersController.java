package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.web.dto.UserContext;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.mappers.ContextMapper;
import net.andresbustamante.yafoot.web.mappers.PlayerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Service REST de gestion des inscriptions des joueurs dans l'application
 *
 * @author andresbustamante
 */
@RestController
@CrossOrigin
public class PlayersController extends AbstractController implements PlayersApi {

    private PlayerManagementService playerManagementService;

    private PlayerSearchService playerSearchService;

    private PlayerMapper playerMapper;

    private ContextMapper contextMapper;

    private HttpServletRequest request;

    @Value("${player.api.service.path}")
    private String playerApiPath;

    private final Logger log = LoggerFactory.getLogger(PlayersController.class);

    @Autowired
    public PlayersController(PlayerManagementService playerManagementService, PlayerSearchService playerSearchService,
                             PlayerMapper playerMapper, ContextMapper contextMapper, HttpServletRequest request) {
        this.playerManagementService = playerManagementService;
        this.playerSearchService = playerSearchService;
        this.playerMapper = playerMapper;
        this.contextMapper = contextMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> createPlayer(Player player) {
        try {
            log.info("New player registration for email address {}", player.getEmail());
            net.andresbustamante.yafoot.model.Player newPlayer = playerMapper.map(player);
            int id = playerManagementService.savePlayer(newPlayer,
                    contextMapper.map(new UserContext()));

            String location = MessageFormat.format(playerApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (ApplicationException e) {
            log.error("User not created", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (DatabaseException | LdapException e) {
            log.error("Database/LDAP error when registering a new player", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> updatePlayer(Player player, Integer id) {
        try {
            log.debug("Player information update requested");
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);
            boolean succes = playerManagementService.updatePlayer(playerMapper.map(player), userContext);
            return (succes) ? ResponseEntity.accepted().build() : ResponseEntity.status(BAD_REQUEST).build();
        } catch (DatabaseException | LdapException e) {
            log.error("An error occurred while updating a player's information", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("User context error for updating a player's information", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Player> loadPlayerByEmail(String email) {
        try {
            net.andresbustamante.yafoot.model.Player player = playerSearchService.findPlayerByEmail(email);

            if (player != null) {
                return ResponseEntity.ok(playerMapper.map(player));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException e) {
            log.error("Database error while looking for a player", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deactivatePlayer(Integer id) {
        try {
            log.debug("Player deactivation requested");
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);
            playerManagementService.deactivatePlayer(id, userContext);
            return ResponseEntity.noContent().build();
        } catch (DatabaseException | LdapException e) {
            log.error("Database/LDAP error while deactivating a player", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("User context error for deactivating a player", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }
}
