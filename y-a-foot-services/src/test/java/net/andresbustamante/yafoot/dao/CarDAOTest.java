package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@DatabaseSetup(value = "classpath:datasets/voituresDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/voituresDataset.xml", type = DELETE_ALL)
class CarDAOTest extends AbstractDAOTest {

    @Autowired
    private CarDAO carDAO;

    private Joueur joueur;

    @BeforeEach
    void setUp() {
        joueur = new Joueur(1);
    }

    @Test
    void findCarById() throws Exception {
        // When
        Voiture voiture1 = carDAO.findCarById(1001);

        // Then
        assertNotNull(voiture1);
        assertEquals("Peugeot 207", voiture1.getNom());
        assertNotNull(voiture1.getNbPlaces());
        assertEquals(4, voiture1.getNbPlaces().intValue());
    }

    @Test
    void saveCar() throws Exception {
        // Given
        Voiture nouvelleVoiture = new Voiture();
        nouvelleVoiture.setNom("Citroën DS3");
        nouvelleVoiture.setNbPlaces(3);

        // When
        int nbLignesInserees = carDAO.saveCar(nouvelleVoiture, joueur);

        // Then
        assertEquals(1, nbLignesInserees);
        assertNotNull(nouvelleVoiture.getId());
        assertTrue(nouvelleVoiture.getId() > 0);
    }

    @Test
    void deleteCarsForPlayer() throws Exception {
        // When
        int nbVoituresSupprimees = carDAO.deleteCarsForPlayer(joueur);

        // Then
        assertEquals(2, nbVoituresSupprimees);
    }

    @Test
    void findCarsByPlayer() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);

        List<Voiture> voitures = carDAO.findCarsByPlayer(joueur);

        assertNotNull(voitures);
        assertEquals(2, voitures.size());

        for (Voiture voiture : voitures) {
            assertNotNull(voiture.getId());
            assertTrue(voiture.getId() > 0);
            assertNotNull(voiture.getNom());
            assertNotNull(voiture.getNbPlaces());
        }
    }
}