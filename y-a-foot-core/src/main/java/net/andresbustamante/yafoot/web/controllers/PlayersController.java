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

    @Value("${players.byid.api.service.path}")
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

    /**
     * @param player
     * @return
     */
    @Override
    public ResponseEntity<Long> createPlayer(Player player) {
        try {
            log.info("Demande de création d'un nouveau joueur avec l'address {}", player.getEmail());
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = playerMapper.map(player);
            int id = playerManagementService.savePlayer(nouveauJoueur,
                    contextMapper.map(new UserContext()));

            String location = MessageFormat.format(playerApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (ApplicationException e) {
            log.error("User not created", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (DatabaseException | LdapException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @param player
     * @return
     */
    @Override
    public ResponseEntity<Void> updatePlayer(Player player, Integer id) {
        try {
            log.debug("Player information update requested");
            net.andresbustamante.yafoot.model.UserContext userContext = getUserContext(request);
            boolean succes = playerManagementService.updatePlayer(playerMapper.map(player), userContext);
            return (succes) ? ResponseEntity.accepted().build() : ResponseEntity.status(BAD_REQUEST).build();
        } catch (DatabaseException | LdapException e) {
            log.error("Erreur lors de l'actualisation d'un joueur", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Player> loadPlayerByEmail(String email) {
        net.andresbustamante.yafoot.model.UserContext userContext = new net.andresbustamante.yafoot.model.UserContext();

        try {
            net.andresbustamante.yafoot.model.Joueur joueur = playerSearchService.findPlayerByEmail(email, userContext);

            if (joueur != null) {
                return ResponseEntity.ok(playerMapper.map(joueur));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur lors de la recherche d'un utilisateur", e);
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
            log.error("Erreur lors de l'actualisation d'un joueur", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }
}
