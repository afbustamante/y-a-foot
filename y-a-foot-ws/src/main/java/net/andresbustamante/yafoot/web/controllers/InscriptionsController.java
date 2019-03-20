package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.web.mappers.InscriptionMapper;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Web Service REST pour la gestion des inscriptions aux matches
 *
 * @author andresbustamante
 */
@Path("/inscriptions")
public class InscriptionsController extends AbstractController {

    @Autowired
    private GestionMatchsService gestionMatchsService;

    private final Logger log = LoggerFactory.getLogger(InscriptionsController.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response inscrireJoueurMatch(Inscription inscription,
                                        @Context HttpServletRequest request) {
        log.info("Traitement de nouvelle demande d'inscription");

        try {
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(request);
            net.andresbustamante.yafoot.model.Inscription ins = InscriptionMapper.INSTANCE.toInscriptionBean(inscription);
            boolean succes = gestionMatchsService.inscrireJoueurMatch(ins.getJoueur(), ins.getMatch(),
                    ins.getVoiture(), contexte);

            if (succes) {
                log.info("Le joueur a ete inscrit");
                String location = MessageFormat.format("/joueurs/{0}", ins.getJoueur().getEmail());
                return Response.created(getLocationURI(location)).build();
            } else {
                log.warn("Le joueur n'a pas pu etre inscrit");
                return Response.status(BAD_REQUEST).build();
            }
        } catch (BDDException e) {
            log.error("Erreur de base de données", e);
            return Response.serverError().build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

}
