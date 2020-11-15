package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.CarSearchService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Car;
import net.andresbustamante.yafoot.web.mappers.CarMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class CarsController extends AbstractController implements CarsApi {

    private static final String CAR_NOT_FOUND_ERROR = "car.not.found.error";

    @Value("api.public.url")
    private String apiPublicUrl;

    private CarSearchService carSearchService;

    private PlayerSearchService playerSearchService;

    private CarManagementService carManagementService;

    private CarMapper carMapper;

    private final Logger log = LoggerFactory.getLogger(CarsController.class);

    @Autowired
    public CarsController(CarSearchService carSearchService, PlayerSearchService playerSearchService,
                          CarManagementService carManagementService,
                          CarMapper carMapper, HttpServletRequest request, ApplicationContext applicationContext) {
        super(request, applicationContext);
        this.carSearchService = carSearchService;
        this.playerSearchService = playerSearchService;
        this.carManagementService = carManagementService;
        this.carMapper = carMapper;
    }

    @Override
    public ResponseEntity<List<Car>> loadCars() {
        try {
            UserContext ctx = getUserContext(request);
            Player player = playerSearchService.findPlayerByEmail(ctx.getUsername());
            List<net.andresbustamante.yafoot.model.Car> cars = carSearchService.findCarsByPlayer(player);

            List<Car> result = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(cars)) {
                result.addAll(carMapper.map(cars));
            }
            return ResponseEntity.ok(result);
        } catch (DatabaseException e) {
            log.error("Error when looking for cars", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ApplicationException e) {
            log.error("Invalid user context", e);
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Car> loadCar(Integer id) {
        try {
            net.andresbustamante.yafoot.model.Car car = carSearchService.loadCar(id, getUserContext(request));

            if (car != null) {
                return ResponseEntity.ok(carMapper.map(car));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(CAR_NOT_FOUND_ERROR, null));
            }
        } catch (ApplicationException e) {
            if (e.getCode() != null && e.getCode().equals(UNAUTHORISED_USER_ERROR)) {
                log.error("User not allowed to load a car from another user", e);
                throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
            } else {
                log.error("Invalid user", e);
                throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
            }
        } catch (DatabaseException e) {
            log.error("Error when looking for cars", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> addNewCar(@Valid Car car) {
        try {
            int carId = carManagementService.saveCar(carMapper.map(car), getUserContext(request));

            return ResponseEntity.created(URI.create(apiPublicUrl + "/cars/" + carId)).build();
        } catch (DatabaseException e) {
            String message = "Database exception when registering a new car";
            log.error(message, e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            String message = "Invalid context for player registering a new car";
            log.error(message, e);
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }
}
