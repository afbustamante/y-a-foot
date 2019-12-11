package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.xs.Registration;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.MatchSearchService;
import net.andresbustamante.yafoot.web.mappers.RegistrationMapper;
import net.andresbustamante.yafoot.web.util.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static net.andresbustamante.yafoot.web.util.RestConstants.MATCH_CODE;

/**
 * Web Service REST pour la gestion des inscriptions aux matches
 *
 * @author andresbustamante
 */
@Path("/registrations")
public class RegistrationsController extends AbstractController {

    @Autowired
    private MatchManagementService matchManagementService;

    @Autowired
    private MatchSearchService matchSearchService;

    @Autowired
    private RegistrationMapper registrationMapper;

    private final Logger log = LoggerFactory.getLogger(RegistrationsController.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerPlayerToMatch(Registration registration,
                                          @Context HttpServletRequest request) {
        log.debug("Traitement de nouvelle demande d'inscription");

        try {
            UserContext userContext = ContextUtils.getUserContext(request);
            net.andresbustamante.yafoot.model.Inscription ins = registrationMapper.map(registration);
            boolean succes = matchManagementService.joinMatch(ins.getJoueur(), ins.getMatch(),
                    ins.getVoiture(), userContext);

            if (succes) {
                log.info("Le joueur a ete inscrit");
                String location = MessageFormat.format("/players/{0}", ins.getJoueur().getEmail());
                return Response.created(getLocationURI(location)).build();
            } else {
                log.warn("Le joueur n'a pas pu etre inscrit");
                return Response.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur de base de données", e);
            return Response.serverError().build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{matchCode}")
    public Response unregisterPlayerFromMatch(@PathParam(MATCH_CODE) String matchCode,
                                              @Context HttpServletRequest request) {
        try {
            UserContext userContext = ContextUtils.getUserContext(request);

            Match match = matchSearchService.findMatchByCode(matchCode, userContext);

            if (match != null) {
                Joueur joueur = new Joueur(userContext.getUserId());
                matchManagementService.quitMatch(joueur, match, userContext);
                return Response.noContent().build();
            } else {
                log.warn("Désinscription demandée sur un match non existant avec le code {}", matchCode);
                return Response.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur de base de données", e);
            return Response.serverError().build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

}
