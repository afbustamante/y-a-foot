package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import net.andresbustamante.yafoot.services.RechercheJoueursService;
import net.andresbustamante.yafoot.web.mappers.ContexteMapper;
import net.andresbustamante.yafoot.web.mappers.JoueurMapper;
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
@Path("/joueurs")
public class JoueursController extends AbstractController {

    @Autowired
    private GestionJoueursService gestionJoueursService;

    @Autowired
    private RechercheJoueursService rechercheJoueursService;

    @Autowired
    private JoueurMapper joueurMapper;

    @Autowired
    private ContexteMapper contexteMapper;

    @Value("${recherche.joueurs.email.service.path}")
    private String pathRechercheJoueursParAdresseMail;

    private final Logger log = LoggerFactory.getLogger(JoueursController.class);

    /**
     * @param joueur
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response inscrireJoueur(Joueur joueur) {
        try {
            log.info("Demande de création d'un nouveau joueur avec l'adresse {}", joueur.getEmail());
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = joueurMapper.toJoueurBean(joueur);
            boolean inscrit = gestionJoueursService.inscrireJoueur(nouveauJoueur,
                    contexteMapper.toContexteBean(new Contexte()));

            if (inscrit) {
                String location = MessageFormat.format(pathRechercheJoueursParAdresseMail, joueur.getEmail());
                return Response.created(getLocationURI(location)).build();
            } else {
                return Response.status(BAD_REQUEST).build();
            }
        } catch (BDDException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            return Response.serverError().build();
        }
    }

    /**
     * @param joueur
     * @param request
     * @return
     */
    @PUT
    @Path("/{email}/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualiserJoueur(@PathParam(EMAIL) String email, Joueur joueur,
                                     @Context HttpServletRequest request) {
        try {
            log.info("Mise à jour des données du joueur {}", email);
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(request);
            boolean succes = gestionJoueursService.actualiserJoueur(joueurMapper.toJoueurBean(joueur), contexte);
            return (succes) ? Response.accepted().build() : Response.status(BAD_REQUEST).build();
        } catch (BDDException e) {
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
                return Response.ok(joueurMapper.toJoueurDTO(joueur)).build();
            } else {
                return Response.status(NOT_FOUND).build();
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche d'un utilisateur", e);
            return Response.serverError().build();
        }
    }
}
