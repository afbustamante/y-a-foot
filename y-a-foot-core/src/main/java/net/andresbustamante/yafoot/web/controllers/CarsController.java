package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.xs.Cars;
import net.andresbustamante.yafoot.model.xs.Car;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.CarSearchService;
import net.andresbustamante.yafoot.web.mappers.CarMapper;
import net.andresbustamante.yafoot.web.util.ContextUtils;
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

import static net.andresbustamante.yafoot.web.util.RestConstants.PLAYER_ID;

@Path("/cars")
public class CarsController extends AbstractController {

    @Value("api.public.url")
    private String apiPublicUrl;

    @Autowired
    private CarSearchService carSearchService;

    @Autowired
    private CarManagementService carManagementService;

    @Autowired
    private CarMapper carMapper;

    private final Logger log = LoggerFactory.getLogger(CarsController.class);

    @GET
    @Path("")
    public Response loadCarList(@QueryParam(PLAYER_ID) Integer idJoueur) {
        try {
            List<net.andresbustamante.yafoot.model.Voiture> cars =
                    carSearchService.findCarsByPlayer(new Joueur(idJoueur), new UserContext());

            Cars carsResponse = new Cars();

            if (CollectionUtils.isNotEmpty(cars)) {
                carsResponse.getCar().addAll(carMapper.map(cars));
            }
            return Response.ok(carsResponse).build();
        } catch (DatabaseException e) {
            log.error("Error when looking for cars", e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("")
    public Response addNewCar(@Valid Car car, @Context HttpServletRequest request) {
        try {
            int carId = carManagementService.saveCar(carMapper.map(car), ContextUtils.getUserContext(request));

            return Response.created(URI.create(apiPublicUrl + "/cars/" + carId)).build();
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
