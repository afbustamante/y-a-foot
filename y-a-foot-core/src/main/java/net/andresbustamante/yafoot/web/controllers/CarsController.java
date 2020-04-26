package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Car;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.CarSearchService;
import net.andresbustamante.yafoot.web.mappers.CarMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.andresbustamante.yafoot.web.controllers.AbstractController.CTX_MESSAGES;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@CrossOrigin(exposedHeaders = {CTX_MESSAGES})
public class CarsController extends AbstractController implements CarsApi {

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
                          CarMapper carMapper, HttpServletRequest request) {
        this.carSearchService = carSearchService;
        this.playerSearchService = playerSearchService;
        this.carManagementService = carManagementService;
        this.carMapper = carMapper;
        this.request = request;
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
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Invalid user context", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
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
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            String message = "Invalid context for player registering a new car";
            log.error(message, e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
