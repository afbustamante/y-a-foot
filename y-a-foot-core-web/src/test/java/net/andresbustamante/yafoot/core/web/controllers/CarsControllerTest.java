package net.andresbustamante.yafoot.core.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarSearchService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.web.mappers.CarMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = {CarsController.class, ObjectMapper.class}, properties = {
        "api.config.public.url=http://myurl",
        "api.cars.root.path=/cars"
}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CarsControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarSearchService carSearchService;

    @MockBean
    private PlayerSearchService playerSearchService;

    @MockBean
    private CarManagementService carManagementService;

    @MockBean
    private CarMapper carMapper;

    @Test
    void loadCars() throws Exception {
        // Given
        Car car1 = new Car(1);
        car1.setName("Car 1");
        Car car2 = new Car(2);
        car2.setName("Car 2");

        given(carSearchService.findCarsByPlayer(any(Player.class))).willReturn(List.of(car1, car2));

        // When
        mvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void loadEmptyCarsList() throws Exception {
        // Given
        given(carSearchService.findCarsByPlayer(any(Player.class))).willReturn(Collections.emptyList());

        // When
        mvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void loadCarsWhileDatabaseIsUnavailable() throws Exception {
        // Given
        given(carSearchService.findCarsByPlayer(any(Player.class))).willThrow(DatabaseException.class);
        given(playerSearchService.findPlayerByEmail(anyString())).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void loadExistingCar() throws Exception {
        // Given
        Integer carId = 1;

        Car car1 = new Car(carId);
        car1.setName("Car 1");

        given(carSearchService.loadCar(anyInt(), any(UserContext.class))).willReturn(car1);
        given(carMapper.map(any(Car.class))).willReturn(new net.andresbustamante.yafoot.web.dto.Car());

        // When
        mvc.perform(get("/cars/{0}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void loadUnknownCar() throws Exception {
        // Given
        Integer carId = 1;

        given(carSearchService.loadCar(anyInt(), any(UserContext.class))).willReturn(null);

        // When
        mvc.perform(get("/cars/{0}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void loadCarFromOtherPlayer() throws Exception {
        // Given
        Integer carId = 1;

        UnauthorisedUserException exception = new UnauthorisedUserException("message");
        given(carSearchService.loadCar(anyInt(), any(UserContext.class))).willThrow(exception);

        // When
        mvc.perform(get("/cars/{0}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    void loadCarWhileDatabaseIsUnavailable() throws Exception {
        // Given
        Integer carId = 1;

        given(carSearchService.loadCar(anyInt(), any(UserContext.class))).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/cars/{0}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addNewCar() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.CarForm car = new net.andresbustamante.yafoot.web.dto.CarForm();
        car.setName("Car 1");
        car.setNumSeats(4);

        Integer id = 1;

        given(carMapper.map(any(net.andresbustamante.yafoot.web.dto.CarForm.class))).willReturn(new Car());
        given(carManagementService.saveCar(any(Car.class), any(UserContext.class))).willReturn(id);

        // When
        mvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car))
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.LOCATION, MessageFormat.format("http://myurl/cars/{0}", id)));
    }

    @Test
    void addNewCarWhileDatabaseIsUnavailable() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car();
        car.setName("Car 1");
        car.setNumSeats(4);

        given(carMapper.map(any(net.andresbustamante.yafoot.web.dto.CarForm.class))).willReturn(new Car());
        given(carManagementService.saveCar(any(Car.class), any(UserContext.class))).willThrow(DatabaseException.class);

        // When
        mvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car))
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }
}
