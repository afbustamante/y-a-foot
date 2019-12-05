package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matches;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
import org.apache.commons.collections4.CollectionUtils;
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
import java.time.ZoneId;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static net.andresbustamante.yafoot.web.util.RestConstants.*;

/**
 * Web Service REST pour la recherche et consultation des matches
 *
 * @author andresbustamante
 */
@Path("/matches")
public class MatchsController extends AbstractController {

    @Autowired
    private RechercheMatchsService rechercheMatchsService;

    @Autowired
    private GestionMatchsService gestionMatchsService;

    @Autowired
    private MatchMapper matchMapper;

    @Value("${matches.bycode.api.service.path}")
    private String pathRechercheMatchsParCode;

    private final Logger log = LoggerFactory.getLogger(MatchsController.class);

    @GET
    @Path("/{matchCode}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getMatchParCode(@PathParam(MATCH_CODE) String matchCode,
                                    @HeaderParam(UTILISATEUR) Integer idUtilisateur) {
        try {
            net.andresbustamante.yafoot.model.Match match = rechercheMatchsService.chercherMatchParCode(matchCode,
                    new Contexte(idUtilisateur));

            return (match != null) ? Response.ok(matchMapper.map(match)).build() :
                    Response.status(NOT_FOUND).build();
        } catch (DatabaseException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getMatchsJoueur(@QueryParam(PLAYER_ID) Integer idJoueur,
                                    @HeaderParam(UTILISATEUR) Integer idUtilisateur,
                                    @HeaderParam(TIMEZONE) String timezone) {
        try {
            Contexte ctx = new Contexte(idUtilisateur);
            ctx.setTimezone(ZoneId.of(timezone));

            List<net.andresbustamante.yafoot.model.Match> matchs = rechercheMatchsService.chercherMatchsJoueur(idJoueur,
                    ctx);

            if (CollectionUtils.isNotEmpty(matchs)) {
                Matches result = new Matches();

                for (net.andresbustamante.yafoot.model.Match m : matchs) {
                    result.getMatch().add(matchMapper.map(m));
                }
                return Response.ok(result).build();
            } else {
                return Response.ok(new Matches()).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return Response.serverError().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response creerMatch(Match match, @Context HttpServletRequest request) {
        try {
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(request);
            net.andresbustamante.yafoot.model.Match m = matchMapper.map(match);
            boolean isMatchCree = gestionMatchsService.creerMatch(m, contexte);

            if (isMatchCree) {
                String location = MessageFormat.format(pathRechercheMatchsParCode, m.getCode());
                return Response.created(getLocationURI(location)).build();
            } else {
                return Response.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur lors de la création d'un match", e);
            return Response.serverError().build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return Response.status(BAD_REQUEST).build();
        }
    }
}
