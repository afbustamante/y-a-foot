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
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class CarsController extends AbstractController implements CarsApi {

    @Value("api.public.url")
    private String apiPublicUrl;

    private CarSearchService carSearchService;

    private CarManagementService carManagementService;

    private CarMapper carMapper;

    private HttpServletRequest request;

    private final Logger log = LoggerFactory.getLogger(CarsController.class);

    @Autowired
    public CarsController(CarSearchService carSearchService, CarManagementService carManagementService,
                          CarMapper carMapper, HttpServletRequest request) {
        this.carSearchService = carSearchService;
        this.carManagementService = carManagementService;
        this.carMapper = carMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Cars> loadCarList(Integer idJoueur) {
        try {
            List<net.andresbustamante.yafoot.model.Voiture> cars =
                    carSearchService.findCarsByPlayer(new Joueur(idJoueur), new UserContext());

            Cars carsResponse = new Cars();

            if (CollectionUtils.isNotEmpty(cars)) {
                carsResponse.getCar().addAll(carMapper.map(cars));
            }
            return ResponseEntity.ok(carsResponse);
        } catch (DatabaseException e) {
            log.error("Error when looking for cars", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> addNewCar(@Valid Car car) {
        try {
            int carId = carManagementService.saveCar(carMapper.map(car), getUserContext(request));

            return ResponseEntity.created(URI.create(apiPublicUrl + "/cars/" + carId)).build();
        } catch (DatabaseException e) {
            String message = "Database exception when registering a new car";
            log.error(message, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            String message = "Invalid context for player registering a new car";
            log.error(message, e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
