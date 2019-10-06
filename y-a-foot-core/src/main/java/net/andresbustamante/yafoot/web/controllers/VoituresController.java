package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.xs.Voiture;
import net.andresbustamante.yafoot.model.xs.Voitures;
import net.andresbustamante.yafoot.services.GestionVoituresService;
import net.andresbustamante.yafoot.services.RechercheVoituresService;
import net.andresbustamante.yafoot.web.mappers.VoitureMapper;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.RestConstants.ID_JOUEUR;

@Path("/voitures")
public class VoituresController extends AbstractController {

    @Value("api.public.url")
    private String apiPublicUrl;

    @Autowired
    private RechercheVoituresService rechercheVoituresService;

    @Autowired
    private GestionVoituresService gestionVoituresService;

    @Autowired
    private VoitureMapper voitureMapper;

    private final Logger log = LoggerFactory.getLogger(VoituresController.class);

    @GET
    @Path("")
    public Response loadCarList(@QueryParam(ID_JOUEUR) Integer idJoueur) {
        try {
            List<net.andresbustamante.yafoot.model.Voiture> cars =
                    rechercheVoituresService.chargerVoituresJoueur(new Joueur(idJoueur), new Contexte());

            Voitures carsResponse = new Voitures();

            if (CollectionUtils.isNotEmpty(cars)) {
                carsResponse.getVoiture().addAll(voitureMapper.map(cars));
            }
            return Response.ok(carsResponse).build();
        } catch (DatabaseException e) {
            log.error("Error when looking for cars", e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("")
    public Response addNewCar(@Valid Voiture car, @Context HttpServletRequest request) {
        try {
            int carId = gestionVoituresService.enregistrerVoiture(voitureMapper.map(car), ContexteUtils.getContexte(request));

            return Response.created(URI.create(apiPublicUrl + "/voitures/" + carId)).build();
        } catch (DatabaseException e) {
            String message = "Database exception when registering a new car";
            log.error(message, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), message).build();
        } catch (ApplicationException e) {
            String message = "Invalid context for player registering a new car";
            log.error(message, e);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), message).build();
        }
    }
}
