package net.andresbustamante.yafoot.core.web.controllers;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarSearchService;
import net.andresbustamante.yafoot.core.web.mappers.CarMapper;
import net.andresbustamante.yafoot.web.dto.Car;
import net.andresbustamante.yafoot.web.dto.CarForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class CarsController extends AbstractController implements CarsApi {

    private static final String CAR_NOT_FOUND_ERROR = "car.not.found.error";

    private final CarSearchService carSearchService;
    private final CarManagementService carManagementService;
    private final CarMapper carMapper;

    public CarsController(
            CarSearchService carSearchService, CarManagementService carManagementService, CarMapper carMapper) {
        this.carSearchService = carSearchService;
        this.carManagementService = carManagementService;
        this.carMapper = carMapper;
    }

    @Override
    public ResponseEntity<List<Car>> loadCars() {
        try {
            UserContext ctx = getUserContext();
            List<net.andresbustamante.yafoot.core.model.Car> cars = carSearchService.findCars(ctx);

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
            net.andresbustamante.yafoot.core.model.Car car = carSearchService.loadCar(id, getUserContext());

            if (car != null) {
                return ResponseEntity.ok(carMapper.map(car));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(CAR_NOT_FOUND_ERROR, null));
            }
        } catch (ApplicationException e) {
            if (UNAUTHORISED_USER_ERROR.equals(e.getCode())) {
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
    public ResponseEntity<Void> addNewCar(@Valid CarForm car) {
        try {
            int carId = carManagementService.saveCar(carMapper.map(car), getUserContext());

            return ResponseEntity.created(getLocationURI("/cars/" + carId)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> updateCar(@Min(1) Integer id, @Valid CarForm carForm) {
        try {
            UserContext userContext = getUserContext();
            net.andresbustamante.yafoot.core.model.Car car = carSearchService.loadCar(id, userContext);

            if (car != null) {
                carManagementService.updateCar(id, carMapper.map(carForm), userContext);
                return ResponseEntity.accepted().build();
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(CAR_NOT_FOUND_ERROR, null));
            }
        } catch (ApplicationException e) {
            if (UNAUTHORISED_USER_ERROR.equals(e.getCode())) {
                throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
            } else {
                throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> deactivateCar(@Min(1) Integer id) {
        try {
            UserContext userContext = getUserContext();
            net.andresbustamante.yafoot.core.model.Car car = carSearchService.loadCar(id, userContext);

            if (car != null) {
                carManagementService.deactivateCar(car, userContext);
                return ResponseEntity.noContent().build();
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(CAR_NOT_FOUND_ERROR, null));
            }
        } catch (ApplicationException e) {
            if (UNAUTHORISED_USER_ERROR.equals(e.getCode())) {
                throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
            } else if ("car.registered.coming.match.error".equals(e.getCode())) {
                throw new ResponseStatusException(CONFLICT, translate(e.getCode(), null));
            } else {
                throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }
}
