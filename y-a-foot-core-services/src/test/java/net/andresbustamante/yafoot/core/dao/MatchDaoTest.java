package net.andresbustamante.yafoot.core.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.commons.config.DbUnitTestConfig;
import net.andresbustamante.yafoot.commons.config.JdbcTestConfig;
import net.andresbustamante.yafoot.commons.dao.AbstractDaoTest;
import net.andresbustamante.yafoot.core.config.MyBatisTestConfig;
import net.andresbustamante.yafoot.core.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.*;
import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.*;
import static net.andresbustamante.yafoot.core.model.enums.SportEnum.FOOTBALL;
import static net.andresbustamante.yafoot.core.model.enums.SportEnum.HANDBALL;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisTestConfig.class, DbUnitTestConfig.class})
@DatabaseSetup(value = "classpath:datasets/matchesDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/matchesDataset.xml", type = DELETE_ALL)
class MatchDaoTest extends AbstractDaoTest {

    @Autowired
    private MatchDao matchDAO;

    @Autowired
    private PlayerDao playerDAO;

    @Autowired
    private SiteDao siteDAO;

    @Autowired
    private CarDao carDAO;

    @Test
    void isCodeAlreadyRegistered() throws Exception {
        // Given
        String registeredCode = "QWERTY-1234";
        String unregisteredCode = "QWERTY-1230";

        // When
        boolean test1 = matchDAO.isCodeAlreadyRegistered(registeredCode);
        boolean test2 = matchDAO.isCodeAlreadyRegistered(unregisteredCode);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    void findMatchByCode() throws Exception {
        // Given
        String code = "QWERTY-1234";
        ZonedDateTime date = ZonedDateTime.of(2019, 10, 2, 19, 10, 0, 0, ZoneId.systemDefault()); // 2019-10-02 19:10

        // When
        Match match = matchDAO.findMatchByCode(code);

        // Then
        assertNotNull(match);
        assertEquals(102, match.getId().intValue());
        assertEquals(date.toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC")),
                match.getDate().atZoneSameInstant(ZoneId.of("UTC")));
        assertEquals(8, match.getNumPlayersMin().intValue());
        assertEquals(12, match.getNumPlayersMax().intValue());
        assertNotNull(match.getCreator());
        assertEquals("Lionel", match.getCreator().getFirstName());
        assertEquals("Messi", match.getCreator().getSurname());
        assertEquals(CREATED, match.getStatus());
        assertNotNull(match.getCreationDate());
        assertEquals(LocalDate.of(2019, 1, 2), match.getCreationDate().toLocalDate());
    }

    @Test
    void loadMatchWithRegistrations() throws Exception {
        // Given
        String code = "QWERTY-1234";

        // When
        Match match = matchDAO.findMatchByCode(code);

        // Then
        assertNotNull(match);
        assertNotNull(match.getRegistrations());
        assertEquals(2, match.getNumRegisteredPlayers().intValue());

        for (Registration ins : match.getRegistrations()) {
            assertNotNull(ins.getPlayer());
            assertNotNull(ins.getId());
        }
    }

    @Test
    void loadMatchWithoutRegistrations() throws Exception {
        // Given
        Integer matchId = 103;

        // When
        Match match = matchDAO.findMatchById(matchId);

        // Then
        assertNotNull(match);
        assertNotNull(match.getRegistrations());
        assertEquals(0, match.getNumRegisteredPlayers().intValue());
    }

    @Test
    void findMatchesByPlayerAndStartDate() throws Exception {
        // Given
        Player player1 = new Player(101);
        OffsetDateTime startDate = LocalDate.of(2018, 10, 2).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player1, null, null, startDate, null);

        // Then
        assertNotNull(matchs);
        assertEquals(1, matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("AZERTY-1234", matchs.get(0).getCode());
        assertTrue(matchs.get(0).getDate().isAfter(startDate));
        assertNotNull(matchs.get(0).getCreator());
        assertEquals("Cristiano", matchs.get(0).getCreator().getFirstName());
        assertEquals("Ronaldo", matchs.get(0).getCreator().getSurname());
        assertEquals("cr7@email.com", matchs.get(0).getCreator().getEmail());
    }

    @Test
    void findMatchesByPlayerAndSport() throws Exception {
        // Given
        Player player2 = new Player(102);

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player2, HANDBALL, null, null, null);

        // Then
        assertNotNull(matchs);
        assertEquals(1, matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("QWERTZ-1234", matchs.get(0).getCode());
        assertNotNull(matchs.get(0).getCreator());
        assertEquals("Lionel", matchs.get(0).getCreator().getFirstName());
        assertEquals("Messi", matchs.get(0).getCreator().getSurname());
        assertEquals("messi@email.com", matchs.get(0).getCreator().getEmail());
        assertNotNull(matchs.get(0).getSport());
        assertEquals(HANDBALL, matchs.get(0).getSport());
    }

    @Test
    void findMatchesByPlayerAndStatus() throws Exception {
        // Given
        Player player2 = new Player(102);

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player2, null, CANCELLED, null, null);

        // Then
        assertNotNull(matchs);
        assertEquals(1, matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("QWERTZ-1234", matchs.get(0).getCode());
        assertNotNull(matchs.get(0).getCreator());
        assertEquals("Lionel", matchs.get(0).getCreator().getFirstName());
        assertEquals("Messi", matchs.get(0).getCreator().getSurname());
        assertEquals("messi@email.com", matchs.get(0).getCreator().getEmail());
        assertNotNull(matchs.get(0).getSport());
        assertEquals(HANDBALL, matchs.get(0).getSport());
        assertNotNull(matchs.get(0).getStatus());
        assertEquals(CANCELLED, matchs.get(0).getStatus());
    }

    @Test
    void findMatchesByPlayerAndEndDate() throws Exception {
        // Given
        Player player2 = new Player(102);
        OffsetDateTime endDate = LocalDate.of(2018, 10, 3).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player2, null, null, null, endDate);

        // Then
        assertNotNull(matchs);
        assertEquals(1, matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("AZERTY-1234", matchs.get(0).getCode());
        assertEquals(LocalDate.of(2018, 10, 2), LocalDate.from(matchs.get(0).getDate()));
        assertNotNull(matchs.get(0).getCreator());
        assertEquals("Cristiano", matchs.get(0).getCreator().getFirstName());
        assertEquals("Ronaldo", matchs.get(0).getCreator().getSurname());
        assertEquals("cr7@email.com", matchs.get(0).getCreator().getEmail());
    }

    @Test
    void findMatchById() throws Exception {
        // Given
        Integer matchId = 101;

        // When
        Match match = matchDAO.findMatchById(matchId);

        //Then
        assertNotNull(match);
        assertEquals("AZERTY-1234", match.getCode());
        assertNotNull(match.getSite());
        assertNotNull(match.getNumPlayersMin());
        assertNotNull(match.getRegistrations());
        assertEquals(101, match.getSite().getId().intValue());
    }

    @Test
    void saveMatch() throws Exception {
        // Given
        OffsetDateTime now = OffsetDateTime.now();
        Player player = playerDAO.findPlayerById(101);
        Site site = siteDAO.findSiteById(101);

        Match match = new Match();
        match.setCode("C-" + (Instant.now().toEpochMilli() / 1000));
        match.setDate(now);
        match.setSport(FOOTBALL);
        match.setNumPlayersMin(10);
        match.setNumPlayersMax(12);
        match.setCarpoolingEnabled(true);
        match.setCodeSharingEnabled(false);
        match.setCreator(player);
        match.setDescription("Match de test");
        match.setSite(site);
        match.setStatus(CREATED);

        // When
        matchDAO.saveMatch(match);

        // Then
        assertNotNull(match.getId());
        assertTrue(match.getId() > 0);
    }

    @Test
    void registerPlayerWithCar() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(101);
        Match match = matchDAO.findMatchById(102);
        Car car = carDAO.findCarById(101);

        // When
        int numLines = matchDAO.registerPlayer(player, match, car, true);

        // Then
        assertEquals(1, numLines);
        assertTrue(matchDAO.isPlayerRegistered(player, match));
    }

    @Test
    void registerPlayerWithNoCar() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(101);
        Match match = matchDAO.findMatchById(102);

        // When
        matchDAO.registerPlayer(player, match, null, false);

        // Then
        assertTrue(matchDAO.isPlayerRegistered(player, match));
    }

    @Test
    void updateCarForRegistration() throws Exception {
        // Given
        Player playerWithCar = playerDAO.findPlayerById(101);
        Player playerWithoutCar = playerDAO.findPlayerById(102);
        Match match = matchDAO.findMatchById(101);

        // When
        Car car = matchDAO.loadRegistration(match, playerWithCar).getCar();
        assertNotNull(car);
        int numLines = matchDAO.updateCarForRegistration(match, playerWithoutCar, car, true);
        Registration registration = matchDAO.loadRegistration(match, playerWithoutCar);

        // Then
        assertEquals(1, numLines);
        assertNotNull(registration);
        assertNotNull(registration.getCar());
        assertEquals(car, registration.getCar());
        assertTrue(registration.isCarConfirmed());
    }

    @Test
    void isPlayerRegistered() throws Exception {
        // Given
        Player registeredPlayer = playerDAO.findPlayerById(101);
        Player unregisteredPlayer = playerDAO.findPlayerById(103);
        Match match = matchDAO.findMatchById(101);

        // When
        boolean test1 = matchDAO.isPlayerRegistered(registeredPlayer, match);
        boolean test2 = matchDAO.isPlayerRegistered(unregisteredPlayer, match);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    void loadRegistration() throws Exception {
        // Given
        Player playerWithCar = playerDAO.findPlayerById(101);
        Player playerWithoutCar = playerDAO.findPlayerById(102);
        Match match = matchDAO.findMatchById(101);

        // When
        Registration registration1 = matchDAO.loadRegistration(match, playerWithCar);
        Registration registration2 = matchDAO.loadRegistration(match, playerWithoutCar);

        // Then
        assertNotNull(registration1);
        assertNotNull(registration1.getId());
        assertNotNull(registration1.getCar());
        assertNotNull(registration1.getCar().getId());
        assertNotNull(registration1.getCar().getName());
        assertNotNull(registration1.getCar().getDriver());
        assertEquals(101, registration1.getCar().getId().intValue());
        assertEquals(playerWithCar, registration1.getCar().getDriver());

        assertNotNull(registration2);
        assertNotNull(registration2.getId());
        assertNull(registration2.getCar());
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(101);
        Match match = matchDAO.findMatchById(101);
        assertTrue(matchDAO.isPlayerRegistered(player, match));

        // When
        matchDAO.unregisterPlayer(player, match);

        // Then
        assertFalse(matchDAO.isPlayerRegistered(player, match));
    }

    @Test
    void unregisterPlayerFromAllMatches() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(101);
        OffsetDateTime startDate = OffsetDateTime.now().minusYears(5L); // 5 years ago

        // When
        int numLines = matchDAO.unregisterPlayerFromAllMatches(player);
        List<Match> matchesByPlayer = matchDAO.findMatchesByPlayer(player, null, null, startDate, null);

        // Then
        assertEquals(1, numLines);
        assertNotNull(matchesByPlayer);
        assertTrue(matchesByPlayer.isEmpty());
    }

    @Test
    void updateStatus() {
        // Given
        int matchId = 101;
        Match match = matchDAO.findMatchById(matchId);
        assertEquals(CREATED, match.getStatus());

        match.setStatus(PLAYED);

        // When
        matchDAO.updateMatchStatus(match);
        Match updatedMatch = matchDAO.findMatchById(matchId);

        // Then
        assertNotNull(updatedMatch);
        assertNotNull(updatedMatch.getStatus());
        assertEquals(match.getStatus(), updatedMatch.getStatus());
        assertTrue(updatedMatch.getLastUpdateDate().isAfter(match.getLastUpdateDate()));
    }

}
