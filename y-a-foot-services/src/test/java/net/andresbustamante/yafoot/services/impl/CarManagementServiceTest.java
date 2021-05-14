package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarManagementServiceTest extends AbstractServiceTest {

    private Player player;

    private UserContext userContext;

    @InjectMocks
    private CarManagementServiceImpl carManagementService;

    @Mock
    private CarDAO carDAO;

    @Mock
    private PlayerDAO playerDAO;

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
    void deleteCarsByPlayer() throws Exception {
        // Given
        Player player = new Player(1);

        // When
        carManagementService.deleteCarsByPlayer(player, userContext);

        // Then
        verify(carDAO).deleteCarsByPlayer(any(Player.class));
    }
}