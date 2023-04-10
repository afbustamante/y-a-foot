package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarManagementServiceTest extends AbstractServiceUnitTest {

    private Player player;

    private UserContext userContext;

    @InjectMocks
    private CarManagementServiceImpl carManagementService;

    @Mock
    private CarDao carDAO;

    @Mock
    private PlayerDao playerDAO;

    @BeforeEach
    void setUp() {
        player = new Player(1);
        userContext = new UserContext();
        userContext.setUsername("test@email.com");
    }

    @Test
    void saveCar() throws Exception {
        // Given
        Car car = new Car(8);
        car.setName("Test car");
        car.setNumSeats(3);

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);

        int carId = carManagementService.saveCar(car, userContext);

        assertEquals(8, carId);
        verify(carDAO).saveCar(any(Car.class));
    }

    @Test
    void updateCar() throws Exception {
        // Given
        int carId = 1000;
        Car storedCar = new Car(carId);
        storedCar.setDriver(player);
        storedCar.setNumSeats(3);
        storedCar.setName("Test car");

        Car updatedCar = new Car(carId);
        updatedCar.setNumSeats(2);
        updatedCar.setName("My car");

        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(carDAO.findCarById(anyInt())).thenReturn(storedCar);

        // When
        assertDoesNotThrow(() -> carManagementService.updateCar(carId, updatedCar, userContext));

        // Then
        verify(carDAO).updateCar(any(Car.class));
    }

    @Test
    void updateCarFromAnotherUser() throws Exception {
        // Given
        int carId = 1000;
        Car storedCar = new Car(carId);
        storedCar.setDriver(new Player(2));
        storedCar.setNumSeats(3);
        storedCar.setName("Test car");

        Car updatedCar = new Car(carId);
        updatedCar.setNumSeats(2);
        updatedCar.setName("My car");

        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(carDAO.findCarById(anyInt())).thenReturn(storedCar);

        // When
        assertThrows(ApplicationException.class, () -> carManagementService.updateCar(carId, updatedCar, userContext));

        // Then
        verify(carDAO, never()).updateCar(any(Car.class));
    }

    @Test
    void deactivateCar() throws Exception {
        // Given
        int carId = 1000;
        Car car = new Car(carId);
        car.setDriver(player);

        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(carDAO.isCarUsedForComingMatches(any(Car.class))).thenReturn(false);

        // When
        assertDoesNotThrow(() -> carManagementService.deactivateCar(car, userContext));

        // Then
        verify(carDAO).deactivateCar(any(Car.class));
    }

    @Test
    void deactivateCarFromAnotherUser() throws Exception {
        // Given
        int carId = 1000;
        Car car = new Car(carId);
        car.setDriver(new Player(2));

        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);

        // When
        assertThrows(ApplicationException.class, () -> carManagementService.deactivateCar(car, userContext));

        // Then
        verify(carDAO, never()).deactivateCar(any(Car.class));
    }

    @Test
    void deactivateCarForFutureMatch() throws Exception {
        // Given
        int carId = 1000;
        Car car = new Car(carId);
        car.setDriver(player);

        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(carDAO.isCarUsedForComingMatches(any(Car.class))).thenReturn(true);

        // When
        assertThrows(ApplicationException.class, () -> carManagementService.deactivateCar(car, userContext));

        // Then
        verify(carDAO, never()).deactivateCar(any(Car.class));
    }

    @Test
    void deactivateCarsByPlayer() throws Exception {
        // When
        carManagementService.deactivateCarsByPlayer(player, userContext);

        // Then
        verify(carDAO).deactivateCarsByPlayer(any(Player.class));
    }
}
