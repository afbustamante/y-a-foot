package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.UserNotAuthorisedException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarSearchService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarsController.class)
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

    @Value("${cars.api.service.path}")
    private String carsApiPath;

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
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
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
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
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
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
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

        // When
        mvc.perform(get("/cars/{0}", carId)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
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
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void loadCarFromOtherPlayer() throws Exception {
        // Given
        Integer carId = 1;

        UserNotAuthorisedException exception = new UserNotAuthorisedException("message");
        given(carSearchService.loadCar(anyInt(), any(UserContext.class))).willThrow(exception);

        // When
        mvc.perform(get("/cars/{0}", carId)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
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
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addNewCar() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car();
        car.setName("Car 1");
        car.setNumSeats(4);

        Integer id = 1;

        given(carManagementService.saveCar(any(Car.class), any(UserContext.class))).willReturn(id);

        // When
        mvc.perform(post("/cars")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car))
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.LOCATION, MessageFormat.format(apiPublicUrl + carsApiPath + "/{0}", id)));
    }

    @Test
    void addNewCarWhileDatabaseIsUnavailable() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car();
        car.setName("Car 1");
        car.setNumSeats(4);

        given(carManagementService.saveCar(any(Car.class), any(UserContext.class))).willThrow(DatabaseException.class);

        // When
        mvc.perform(post("/cars")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car))
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }
}