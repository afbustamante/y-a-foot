package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class CarManagementServiceImplTest extends AbstractServiceTest {

    private Joueur joueur;

    private UserContext userContext;

    @InjectMocks
    private CarManagementServiceImpl carManagementService;

    @Mock
    private CarDAO carDAO;

    @BeforeEach
    void setUp() {
        joueur = new Joueur(1);
        userContext = new UserContext();
        userContext.setUserId(joueur.getId());
    }

    @Test
    void saveCar() throws Exception {
        // Given
        Voiture voiture = new Voiture(8);
        voiture.setNom("Test car");
        voiture.setNbPlaces(3);

        int idVoiture = carManagementService.saveCar(voiture, userContext);

        assertEquals(8, idVoiture);
        verify(carDAO).saveCar(any(Voiture.class), any(Joueur.class));

    }
}