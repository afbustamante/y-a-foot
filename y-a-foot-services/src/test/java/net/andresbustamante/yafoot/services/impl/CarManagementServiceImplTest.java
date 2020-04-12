package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Voiture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CarManagementServiceImplTest extends AbstractServiceTest {

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
        Voiture voiture = new Voiture(8);
        voiture.setNom("Test car");
        voiture.setNbPlaces(3);

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);

        int idVoiture = carManagementService.saveCar(voiture, userContext);

        assertEquals(8, idVoiture);
        verify(carDAO).saveCar(any(Voiture.class), any(Player.class));
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