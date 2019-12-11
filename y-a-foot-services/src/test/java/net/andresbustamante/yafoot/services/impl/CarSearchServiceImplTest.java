package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
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
        Joueur joueur = new Joueur(1);
        List<Voiture> voitures = Arrays.asList(new Voiture(1), new Voiture(2));
        when(carDAO.findCarsByPlayer(any(Joueur.class))).thenReturn(voitures);

        // When
        List<Voiture> voituresCharges = carSearchService.findCarsByPlayer(joueur, new UserContext());

        // Then
        assertNotNull(voituresCharges);
        assertEquals(voitures, voituresCharges);

        verify(carDAO).findCarsByPlayer(any(Joueur.class));
    }
}