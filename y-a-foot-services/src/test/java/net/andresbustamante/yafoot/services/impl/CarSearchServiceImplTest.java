package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;

class CarSearchServiceImplTest extends AbstractServiceTest {

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
        List<Car> voituresCharges = carSearchService.findCarsByPlayer(player);

        // Then
        assertNotNull(voituresCharges);
        assertEquals(cars, voituresCharges);

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