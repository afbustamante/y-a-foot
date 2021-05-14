package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.core.dao.CarDAO;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.impl.CarSearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarSearchServiceTest extends AbstractServiceTest {

    @InjectMocks
    private CarSearchServiceImpl carSearchService;

    @Mock
    private CarDAO carDAO;

    @Test
    void findCarsByPlayer() throws Exception {
        // Given
        Player player = new Player(1);
        List<Car> cars = Arrays.asList(new Car(1), new Car(2));
        when(carDAO.findCarsByPlayer(any(Player.class))).thenReturn(cars);

        // When
        List<Car> carsFound = carSearchService.findCarsByPlayer(player);

        // Then
        assertNotNull(carsFound);
        assertEquals(cars, carsFound);

        verify(carDAO).findCarsByPlayer(any(Player.class));
    }

    @Test
    void loadCarForDriver() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        UserContext ctx = new UserContext("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);

        Car result = carSearchService.loadCar(1, ctx);

        // Then
        assertNotNull(result);
        verify(carDAO).findCarById(anyInt());
    }

    @Test
    void loadCarForAnotherUser() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        UserContext ctx = new UserContext("someoneelse@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);

        // Then
        assertThrows(ApplicationException.class, () -> carSearchService.loadCar(1, ctx));
        verify(carDAO).findCarById(anyInt());
    }
}