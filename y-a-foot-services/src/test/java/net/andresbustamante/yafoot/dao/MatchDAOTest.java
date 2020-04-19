package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Inscription;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@DatabaseSetup(value = "classpath:datasets/matchesDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/matchesDataset.xml", type = DELETE_ALL)
class MatchDAOTest extends AbstractDAOTest {

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private PlayerDAO playerDAO;

    @Autowired
    private SiteDAO siteDAO;

    @Test
    void isCodeAlreadyRegistered() throws Exception {
        // Given
        String codeExistant = "QWERTY-1234";
        String codeInexistant = "QWERTY-1230";

        // When
        boolean test1 = matchDAO.isCodeAlreadyRegistered(codeExistant);
        boolean test2 = matchDAO.isCodeAlreadyRegistered(codeInexistant);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    void findMatchByCode() throws Exception {
        // Given
        String code = "QWERTY-1234";
        LocalDateTime dateMatch = LocalDateTime.of(2019, 10, 2, 19, 10); // 2019-10-02 19:10

        // When
        Match match = matchDAO.findMatchByCode(code);

        // Then
        assertNotNull(match);
        assertEquals(2, match.getId().intValue());
        assertEquals(ZonedDateTime.of(dateMatch, ZoneId.systemDefault()), match.getDate());
        assertEquals(8, match.getNumPlayersMin().intValue());
        assertEquals(12, match.getNumPlayersMax().intValue());
        assertNotNull(match.getCreator());
        assertEquals("Lionel", match.getCreator().getFirstName());
        assertEquals("Messi", match.getCreator().getSurname());
    }

    @Test
    void chercherMatchsAvecInscriptions() throws Exception {
        // Given
        String code = "QWERTY-1234";

        // When
        Match match = matchDAO.findMatchByCode(code);

        // Then
        assertNotNull(match);
        assertNotNull(match.getRegistrations());
        assertEquals(2, match.getRegistrations().size());

        for (Inscription ins : match.getRegistrations()) {
            assertNull(ins.getMatch());
            assertNotNull(ins.getPlayer());
            assertNotNull(ins.getId());
        }
    }

    @Test
    void chercherMatchsSansInscriptions() throws Exception {
        // Given
        Integer matchId = 3;

        // When
        Match match = matchDAO.findMatchById(matchId);

        // Then
        assertNotNull(match);
        assertNotNull(match.getRegistrations());
        assertTrue(match.getRegistrations().isEmpty());
    }

    @Test
    void findMatchesByPlayerAndStartDate() throws Exception {
        // Given
        Player player1 = new Player(1);
        ZonedDateTime startDate = LocalDate.of(2018, 10, 2).atStartOfDay(ZoneId.systemDefault());

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player1, startDate, null);

        // Then
        assertNotNull(matchs);
        assertEquals(1 ,matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("AZERTY-1234", matchs.get(0).getCode());
        assertTrue(matchs.get(0).getDate().isAfter(startDate));
        assertNotNull(matchs.get(0).getCreator());
        assertEquals("Cristiano", matchs.get(0).getCreator().getFirstName());
        assertEquals("Ronaldo", matchs.get(0).getCreator().getSurname());
        assertEquals("cr7@email.com", matchs.get(0).getCreator().getEmail());
    }

    @Test
    void findMatchesByPlayerAndEndDate() throws Exception {
        // Given
        Player player2 = new Player(2);
        ZonedDateTime endDate = LocalDate.of(2018, 10, 3).atStartOfDay(ZoneId.systemDefault());

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player2, null, endDate);

        // Then
        assertNotNull(matchs);
        assertEquals(1 ,matchs.size());
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
        Integer matchId = 1;

        // When
        Match match = matchDAO.findMatchById(1);

        //Then
        assertNotNull(match);
        assertEquals("AZERTY-1234", match.getCode());
        assertNotNull(match.getSite());
        assertNotNull(match.getNumPlayersMin());
        assertNotNull(match.getNumRegisteredPlayers());
        assertEquals(1, match.getSite().getId().intValue());
    }

    @Test
    void saveMatch() throws Exception {
        // Given
        ZonedDateTime maintenant = ZonedDateTime.now();
        Player player = playerDAO.findPlayerById(1);
        Site site = siteDAO.findSiteById(1);

        Match match = new Match();
        match.setCode("C-" + (Instant.now().toEpochMilli() / 1000));
        match.setDate(maintenant);
        match.setNumPlayersMin(10);
        match.setNumPlayersMax(12);
        match.setCarpoolingEnabled(true);
        match.setCodeSharingEnabled(false);
        match.setCreator(player);
        match.setDescription("Match de test");
        match.setSite(site);

        // When
        matchDAO.saveMatch(match);

        // Then
        assertNotNull(match.getId());
        assertTrue(match.getId() > 0);
    }

    @Test
    void registerPlayerWithNoCar() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(1);
        Match match = matchDAO.findMatchById(2);

        // When
        matchDAO.registerPlayer(player, match, null);

        // Then
        assertTrue(matchDAO.isPlayerRegistered(player, match));
    }

    @Test
    void isPlayerRegistered() throws Exception {
        // Given
        Player registeredPlayer = playerDAO.findPlayerById(1);
        Player unregisteredPlayer = playerDAO.findPlayerById(3);
        Match match = matchDAO.findMatchById(1);

        // When
        boolean test1 = matchDAO.isPlayerRegistered(registeredPlayer, match);
        boolean test2 = matchDAO.isPlayerRegistered(unregisteredPlayer, match);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(1);
        Match match = matchDAO.findMatchById(1);
        assertTrue(matchDAO.isPlayerRegistered(player, match));

        // When
        matchDAO.unregisterPlayer(player, match);

        // Then
        assertFalse(matchDAO.isPlayerRegistered(player, match));
    }

    @Test
    void unregisterPlayerFromAllMatches() throws Exception {
        // Given
        Player player = playerDAO.findPlayerById(1);
        ZonedDateTime startDate = ZonedDateTime.now().minusYears(5L); // 5 years ago

        // When
        int numLines = matchDAO.unregisterPlayerFromAllMatches(player);
        List<Match> matchesByPlayer = matchDAO.findMatchesByPlayer(player, startDate, null);

        // Then
        assertEquals(1, numLines);
        assertNotNull(matchesByPlayer);
        assertTrue(matchesByPlayer.isEmpty());
    }

    @Test
    void notifyPlayerRegistry() throws Exception {
        // Given
        Match match = matchDAO.findMatchById(1);
        assertEquals(1, match.getNumRegisteredPlayers().intValue());

        // When
        int numMatches = matchDAO.notifyPlayerRegistry(match);
        match = matchDAO.findMatchById(1);

        // Then
        assertEquals(1, numMatches);
        assertEquals(2, match.getNumRegisteredPlayers().intValue());
    }
}