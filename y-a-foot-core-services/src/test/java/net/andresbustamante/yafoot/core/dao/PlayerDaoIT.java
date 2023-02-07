package net.andresbustamante.yafoot.core.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.commons.config.DbUnitTestConfig;
import net.andresbustamante.yafoot.commons.config.JdbcTestConfig;
import net.andresbustamante.yafoot.commons.dao.AbstractDaoIntegrationTest;
import net.andresbustamante.yafoot.core.config.MyBatisTestConfig;
import net.andresbustamante.yafoot.core.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisTestConfig.class, DbUnitTestConfig.class})
@DatabaseSetup(value = "classpath:datasets/players/t_player.csv")
@DatabaseTearDown(value = "classpath:datasets/players/t_player.csv", type = DELETE_ALL)
class PlayerDaoIT extends AbstractDaoIntegrationTest {

    private static final String EMAIL = "john.doe@email.com";
    private static final String PHONE_NUMBER = "0423456789";
    private static final String SURNAME = "Smith";
    private static final String FIRST_NAME = "Alan";
    private static final String NEW_EMAIL = "nonInscrit@email.com";

    private static final Player JOHN_DOE = new Player(101, "Doe", "John", EMAIL, "01234656789");

    @Autowired
    private PlayerDao playerDAO;

    @Test
    void savePlayer() throws Exception {
        Player player = buildNewPlayer();
        int numPlayers = playerDAO.savePlayer(player);

        assertEquals(1, numPlayers);
        assertNotNull(player.getId());
        assertTrue(player.getId() > 0);
    }

    @Test
    void isPlayerAlreadySignedIn() throws Exception {
        boolean alreadySignedUp = playerDAO.isPlayerAlreadySignedUp(NEW_EMAIL);
        assertFalse(alreadySignedUp);

        alreadySignedUp = playerDAO.isPlayerAlreadySignedUp(EMAIL);
        assertTrue(alreadySignedUp);
    }

    @Test
    void findPlayerById() throws Exception {
        Player player1 = playerDAO.findPlayerById(JOHN_DOE.getId());
        assertNotNull(player1);

        assertNotNull(player1.getSurname());
        assertEquals(JOHN_DOE.getSurname(), player1.getSurname());
        assertNotNull(player1.getFirstName());
        assertEquals(JOHN_DOE.getFirstName(), player1.getFirstName());
        assertNotNull(player1.getPhoneNumber());
        assertNotNull(player1.getEmail());
        assertEquals(JOHN_DOE.getPhoneNumber(), player1.getPhoneNumber());
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
        assertNotNull(player.getCreationDate());
    }

    @Test
    void updatePlayer() throws Exception {
        Player player = playerDAO.findPlayerByEmail(EMAIL);
        player.setPhoneNumber(PHONE_NUMBER);
        player.setSurname(SURNAME);
        player.setFirstName(FIRST_NAME);
        assertNull(player.getLastUpdateDate());

        int numPlayers = playerDAO.updatePlayer(player);

        Player player1 = playerDAO.findPlayerByEmail(EMAIL);

        assertEquals(1, numPlayers);
        assertNotNull(player1.getPhoneNumber());
        assertEquals(PHONE_NUMBER, player1.getPhoneNumber());
        assertNotNull(player1.getSurname());
        assertEquals(SURNAME, player1.getSurname());
        assertNotNull(player1.getFirstName());
        assertEquals(FIRST_NAME, player1.getFirstName());
    }

    @Test
    void deletePlayer() throws Exception {
        // When
        int numPlayers = playerDAO.deletePlayer(JOHN_DOE);
        Player player = playerDAO.findPlayerByEmail(EMAIL);

        // Then
        assertEquals(1, numPlayers);
        assertNull(player);
    }

    @Test
    void deactivatePlayer() throws Exception {
        // When
        int numPlayers = playerDAO.deactivatePlayer(JOHN_DOE);
        Player player = playerDAO.findPlayerById(JOHN_DOE.getId());

        // Then
        assertEquals(1, numPlayers);
        assertNotNull(player);
        assertTrue(player.getFirstName().startsWith("User"));
        assertTrue(player.getFirstName().endsWith(player.getId().toString()));
        assertEquals("Foot", player.getSurname());
        assertFalse(player.isActive());
        assertNull(player.getPhoneNumber());
    }

    /**
     * Initialises a new temporary player.
     *
     * @return Temporary test player
     */
    private Player buildNewPlayer() {
        Player player = new Player();
        player.setEmail("hope.solo@test.com");
        player.setFirstName("Hope");
        player.setSurname("Solo");
        player.setPhoneNumber("0122334455");
        return player;
    }
}
