package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.xs.Player;
import net.andresbustamante.yafoot.model.xs.UserContext;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import net.andresbustamante.yafoot.services.RechercheJoueursService;
import net.andresbustamante.yafoot.web.mappers.ContextMapper;
import net.andresbustamante.yafoot.web.mappers.PlayerMapper;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
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
public class JoueursController extends AbstractController {

    @Autowired
    private GestionJoueursService gestionJoueursService;

    @Autowired
    private RechercheJoueursService rechercheJoueursService;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private ContextMapper contextMapper;

    @Value("${players.byemail.api.service.path}")
    private String pathRechercheJoueursParAdresseMail;

    private final Logger log = LoggerFactory.getLogger(JoueursController.class);

    /**
     * @param player
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response inscrireJoueur(Player player) {
        try {
            log.info("Demande de création d'un nouveau joueur avec l'address {}", player.getEmail());
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = playerMapper.map(player);
            boolean inscrit = gestionJoueursService.inscrireJoueur(nouveauJoueur,
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
    public Response actualiserJoueur(@PathParam(EMAIL) String email, Player player,
                                     @Context HttpServletRequest request) {
        try {
            log.info("Mise à jour des données du joueur {}", email);
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(request);
            boolean succes = gestionJoueursService.actualiserJoueur(playerMapper.map(player), contexte);
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
    public Response chercherJoueurParEmail(@PathParam(EMAIL) String email) {
        net.andresbustamante.yafoot.model.Contexte contexte = new net.andresbustamante.yafoot.model.Contexte();

        try {
            net.andresbustamante.yafoot.model.Joueur joueur = rechercheJoueursService.chercherJoueur(email, contexte);

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
    public Response desactiverJoueur(@PathParam(EMAIL) String email,
                                     @Context HttpServletRequest request) {
        try {
            log.info("Traitement de la demande de désactivation du joueur {}", email);
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(request);
            boolean succes = gestionJoueursService.desactiverJoueur(email, contexte);
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
