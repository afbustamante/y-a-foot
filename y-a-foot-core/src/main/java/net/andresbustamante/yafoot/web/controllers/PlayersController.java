package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.exceptions.PlayerNotFoundException;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.mappers.PlayerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Optional;

import static net.andresbustamante.yafoot.web.controllers.AbstractController.CTX_MESSAGES;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * REST Controller to manage operations on players
 *
 * @author andresbustamante
 */
@RestController
@CrossOrigin(exposedHeaders = {CTX_MESSAGES})
public class PlayersController extends AbstractController implements PlayersApi {

    private PlayerManagementService playerManagementService;

    private PlayerSearchService playerSearchService;

    private PlayerMapper playerMapper;

    @Value("${player.api.service.path}")
    private String playerApiPath;

    private final Logger log = LoggerFactory.getLogger(PlayersController.class);

    @Autowired
    public PlayersController(PlayerManagementService playerManagementService, PlayerSearchService playerSearchService,
                             PlayerMapper playerMapper, HttpServletRequest request,
                             ApplicationContext applicationContext) {
        super(request, applicationContext);
        this.playerManagementService = playerManagementService;
        this.playerSearchService = playerSearchService;
        this.playerMapper = playerMapper;
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
            int id = playerManagementService.savePlayer(newPlayer, new net.andresbustamante.yafoot.model.UserContext());

            String location = MessageFormat.format(playerApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (ApplicationException e) {
            log.error("User not created", e);
            return new ResponseEntity<>(buildMessageHeader("email.already.registered.error", new String[]{player.getEmail()}), BAD_REQUEST);
        } catch (DatabaseException | LdapException e) {
            log.error("Database/LDAP error when registering a new player", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> updatePlayer(Integer id, Player player) {
        try {
            log.debug("Player information update requested");
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);
            playerManagementService.updatePlayer(playerMapper.map(player), userContext);
            return ResponseEntity.accepted().build();
        } catch (DatabaseException | LdapException e) {
            log.error("An error occurred while updating a player's information", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (PlayerNotFoundException e) {
            log.error("Player not found for performing update", e);
            return ResponseEntity.notFound().build();
        } catch (ApplicationException e) {
            log.error("User context error for updating a player's information", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
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
            log.error("Database error while looking for a player", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> deactivatePlayer(Integer id) {
        try {
            log.debug("Player deactivation requested");
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.model.Player player = playerSearchService.findPlayerById(id);

            if (player != null) {
                playerManagementService.deactivatePlayer(player, userContext);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException | LdapException e) {
            log.error("Database/LDAP error while deactivating a player", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("User context error for deactivating a player", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }
}
