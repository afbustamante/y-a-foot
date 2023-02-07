package net.andresbustamante.yafoot.core.services.impl;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void deleteCarsByPlayer() throws Exception {
        // When
        carManagementService.deleteCarsByPlayer(player, userContext);

        // Then
        verify(carDAO).deleteCarsByPlayer(any(Player.class));
    }
}
