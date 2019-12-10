package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.model.Contexte;
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

    private Contexte contexte;

    @InjectMocks
    private CarManagementServiceImpl gestionVoituresService;

    @Mock
    private VoitureDAO voitureDAO;

    @BeforeEach
    void setUp() {
        joueur = new Joueur(1);
        contexte = new Contexte();
        contexte.setIdUtilisateur(joueur.getId());
    }

    @Test
    void enregistrerVoiture() throws Exception {
        // Given
        Voiture voiture = new Voiture(8);
        voiture.setNom("Test car");
        voiture.setNbPlaces(3);

        int idVoiture = gestionVoituresService.saveCar(voiture, contexte);

        assertEquals(8, idVoiture);
        verify(voitureDAO).enregistrerVoiture(any(Voiture.class), any(Joueur.class));

    }
}