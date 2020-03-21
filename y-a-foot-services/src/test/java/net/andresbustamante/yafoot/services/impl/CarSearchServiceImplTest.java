package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Voiture;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

class CarSearchServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private CarSearchServiceImpl carSearchService;

    @Mock
    private CarDAO carDAO;

    @Test
    void findCarsByPlayer() throws Exception {
        // Given
        Player player = new Player(1);
        List<Voiture> voitures = Arrays.asList(new Voiture(1), new Voiture(2));
        when(carDAO.findCarsByPlayer(any(Player.class))).thenReturn(voitures);

        // When
        List<Voiture> voituresCharges = carSearchService.findCarsByPlayer(player);

        // Then
        assertNotNull(voituresCharges);
        assertEquals(voitures, voituresCharges);

        verify(carDAO).findCarsByPlayer(any(Player.class));
    }
}