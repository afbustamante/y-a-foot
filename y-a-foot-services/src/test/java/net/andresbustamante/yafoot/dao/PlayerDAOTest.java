package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@DatabaseSetup(value = "classpath:datasets/joueursDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/joueursDataset.xml", type = DELETE_ALL)
class PlayerDAOTest extends AbstractDAOTest {

    private static final String EMAIL = "john.doe@email.com";
    private static final String AUTRE_TELEPHONE = "0423456789";
    private static final String AUTRE_NOM = "Smith";
    private static final String AUTRE_PRENOM = "Alan";
    private static final String NOUVEL_EMAIL = "nonInscrit@email.com";

    private static final Player JOHN_DOE = new Player(1, "Doe", "John", EMAIL, "01234656789");

    @Autowired
    private PlayerDAO playerDAO;

    @Test
    void savePlayer() throws Exception {
        Player player = getNouveauJoueur();
        int nbJoueurs = playerDAO.savePlayer(player);

        // Vérifier que le player à un identifiant de base de données attribué
        assertEquals(1, nbJoueurs);
        assertNotNull(player.getId());
        assertTrue(player.getId() > 0);
    }

    @Test
    void isPlayerAlreadySignedIn() throws Exception {
        boolean isInscrit = playerDAO.isPlayerAlreadySignedIn(NOUVEL_EMAIL);
        assertFalse(isInscrit);

        isInscrit = playerDAO.isPlayerAlreadySignedIn(EMAIL);
        assertTrue(isInscrit);
    }

    @Test
    void findPlayerById() throws Exception {
        Player player1 = playerDAO.findPlayerById(JOHN_DOE.getId());
        assertNotNull(player1);

        // Vérifier que les informations sont complètes
        assertNotNull(player1.getSurname());
        assertEquals(JOHN_DOE.getSurname(), player1.getSurname());
        assertNotNull(player1.getFirstName());
        assertEquals(JOHN_DOE.getFirstName(), player1.getFirstName());
        assertNotNull(player1.getPhoneNumber());
        assertNotNull(player1.getEmail());
        assertEquals(JOHN_DOE.getPhoneNumber(), player1.getPhoneNumber());
        assertNotNull(player1.getDateCreation());
        assertEquals(LocalDateTime.of(2019, 1, 2, 12, 34, 56), player1.getDateCreation());
    }

    @Test
    void findPlayerByEmail() throws Exception {
        Player player = playerDAO.findPlayerByEmail(EMAIL);
        assertNotNull(player);
        assertNotNull(player.getSurname());
        assertEquals(JOHN_DOE.getSurname(), player.getSurname());
        assertNotNull(player.getFirstName());
        assertEquals(JOHN_DOE.getFirstName(), player.getFirstName());
        assertNotNull(player.getPhoneNumber());
        assertEquals(JOHN_DOE.getPhoneNumber(), player.getPhoneNumber());
        assertNotNull(player.getEmail());
        assertEquals(JOHN_DOE.getEmail(), player.getEmail());
        assertNotNull(player.getDateCreation());
    }

    @Test
    void updatePlayer() throws Exception {
        // Modifier les informations pour le player avec l'ID de John Doe
        Player player = playerDAO.findPlayerByEmail(EMAIL);
        player.setPhoneNumber(AUTRE_TELEPHONE);
        player.setSurname(AUTRE_NOM);
        player.setFirstName(AUTRE_PRENOM);
        assertNull(player.getDateDerniereMaj());

        int nbJoueurs = playerDAO.updatePlayer(player);

        Player player1 = playerDAO.findPlayerByEmail(EMAIL);

        // Les informations du player 1 doivent être modifiées
        assertEquals(1, nbJoueurs);
        assertNotNull(player1.getPhoneNumber());
        assertEquals(AUTRE_TELEPHONE, player1.getPhoneNumber());
        assertNotNull(player1.getSurname());
        assertEquals(AUTRE_NOM, player1.getSurname());
        assertNotNull(player1.getFirstName());
        assertEquals(AUTRE_PRENOM, player1.getFirstName());
        assertNotNull(player1.getDateDerniereMaj());
        assertTrue(player.getDateCreation().isBefore(player1.getDateDerniereMaj()));
    }

    @Test
    void deletePlayer() throws Exception {
        // When
        int nbJoueurs = playerDAO.deletePlayer(JOHN_DOE);
        Player player = playerDAO.findPlayerByEmail(EMAIL);

        // Then
        assertEquals(1, nbJoueurs);
        // Le player n'existe plus
        assertNull(player);
    }

    @Test
    void deactivatePlayer() throws Exception {
        // When
        int nbJoueurs = playerDAO.deactivatePlayer(JOHN_DOE);
        Player player = playerDAO.findPlayerById(JOHN_DOE.getId());

        // Then
        assertEquals(1, nbJoueurs);
        assertNotNull(player);
        assertTrue(player.getFirstName().startsWith("User"));
        assertTrue(player.getFirstName().endsWith(player.getId().toString()));
        assertEquals("Foot", player.getSurname());
        assertFalse(player.isActive());
        assertNull(player.getPhoneNumber());
        assertTrue(player.getEmail().startsWith(JOHN_DOE.getEmail()));
        assertNotEquals(JOHN_DOE.getEmail(), player.getEmail());
        assertTrue(player.getEmail().endsWith(".old"));
    }

    /**
     * Créer un nouveau joueur temporaire
     *
     * @return Nouveau joueur avec les informations de base
     */
    private Player getNouveauJoueur() {
        Player player = new Player();
        player.setEmail("hope.solo@test.com");
        player.setFirstName("Hope");
        player.setSurname("Solo");
        player.setPhoneNumber("0122334455");
        return player;
    }
}