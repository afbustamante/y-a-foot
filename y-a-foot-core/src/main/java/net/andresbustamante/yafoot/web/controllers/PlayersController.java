package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.xs.Player;
import net.andresbustamante.yafoot.model.xs.UserContext;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.mappers.ContextMapper;
import net.andresbustamante.yafoot.web.mappers.PlayerMapper;
import net.andresbustamante.yafoot.web.util.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static net.andresbustamante.yafoot.web.util.RestConstants.EMAIL;

/**
 * Service REST de gestion des inscriptions des joueurs dans l'application
 *
 * @author andresbustamante
 */
@Path("/players")
public class PlayersController extends AbstractController {

    @Autowired
    private PlayerManagementService playerManagementService;

    @Autowired
    private PlayerSearchService playerSearchService;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private ContextMapper contextMapper;

    @Value("${players.byemail.api.service.path}")
    private String pathRechercheJoueursParAdresseMail;

    private final Logger log = LoggerFactory.getLogger(PlayersController.class);

    /**
     * @param player
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPlayer(Player player) {
        try {
            log.info("Demande de création d'un nouveau joueur avec l'address {}", player.getEmail());
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = playerMapper.map(player);
            boolean inscrit = playerManagementService.savePlayer(nouveauJoueur,
                    contextMapper.map(new UserContext()));

            if (inscrit) {
                String location = MessageFormat.format(pathRechercheJoueursParAdresseMail, player.getEmail());
                return Response.created(getLocationURI(location)).build();
            } else {
                return Response.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException | LdapException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            return Response.serverError().build();
        }
    }

    /**
     * @param player
     * @param request
     * @return
     */
    @PUT
    @Path("/{email}/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlayer(@PathParam(EMAIL) String email, Player player,
                                 @Context HttpServletRequest request) {
        try {
            log.debug("Player information update requested");
            net.andresbustamante.yafoot.model.UserContext userContext = ContextUtils.getUserContext(request);
            boolean succes = playerManagementService.updatePlayer(playerMapper.map(player), userContext);
            return (succes) ? Response.accepted().build() : Response.status(BAD_REQUEST).build();
        } catch (DatabaseException | LdapException e) {
            log.error("Erreur lors de l'actualisation d'un joueur", e);
            return Response.serverError().build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{email}/email")
    @Produces(MediaType.APPLICATION_XML)
    public Response loadPlayerByEmail(@PathParam(EMAIL) String email) {
        net.andresbustamante.yafoot.model.UserContext userContext = new net.andresbustamante.yafoot.model.UserContext();

        try {
            net.andresbustamante.yafoot.model.Joueur joueur = playerSearchService.findPlayerByEmail(email, userContext);

            if (joueur != null) {
                return Response.ok(playerMapper.map(joueur)).build();
            } else {
                return Response.status(NOT_FOUND).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur lors de la recherche d'un utilisateur", e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{email}/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivatePlayer(@PathParam(EMAIL) String email,
                                     @Context HttpServletRequest request) {
        try {
            log.debug("Player deactivation requested");
            net.andresbustamante.yafoot.model.UserContext userContext = ContextUtils.getUserContext(request);
            boolean succes = playerManagementService.deactivatePlayer(email, userContext);
            return (succes) ? Response.noContent().build() : Response.status(BAD_REQUEST).build();
        } catch (DatabaseException | LdapException e) {
            log.error("Erreur lors de l'actualisation d'un joueur", e);
            return Response.serverError().build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return Response.status(BAD_REQUEST).build();
        }
    }
}