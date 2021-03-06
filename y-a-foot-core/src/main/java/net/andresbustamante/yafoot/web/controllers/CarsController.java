package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarSearchService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Car;
import net.andresbustamante.yafoot.web.mappers.CarMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class CarsController extends AbstractController implements CarsApi {

    private static final String CAR_NOT_FOUND_ERROR = "car.not.found.error";

    private CarSearchService carSearchService;

    private PlayerSearchService playerSearchService;

    private CarManagementService carManagementService;

    private CarMapper carMapper;

    @Autowired
    public CarsController(CarSearchService carSearchService, PlayerSearchService playerSearchService,
                          CarManagementService carManagementService,
                          CarMapper carMapper, HttpServletRequest request, ObjectMapper objectMapper,
                          ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
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
            List<net.andresbustamante.yafoot.core.model.Car> cars = carSearchService.findCarsByPlayer(player);

            List<Car> result = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(cars)) {
                result.addAll(carMapper.map(cars));
            }
            return ResponseEntity.ok(result);
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Car> loadCar(Integer id) {
        try {
            net.andresbustamante.yafoot.core.model.Car car = carSearchService.loadCar(id, getUserContext(request));

            if (car != null) {
                return ResponseEntity.ok(carMapper.map(car));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(CAR_NOT_FOUND_ERROR, null));
            }
        } catch (ApplicationException e) {
            if (e.getCode() != null && e.getCode().equals(UNAUTHORISED_USER_ERROR)) {
                throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
            } else {
                throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> addNewCar(@Valid Car car) {
        try {
            int carId = carManagementService.saveCar(carMapper.map(car), getUserContext(request));

            return ResponseEntity.created(getLocationURI("/cars/" + carId)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }
}
