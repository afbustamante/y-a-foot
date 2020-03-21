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

@DatabaseSetup(value = "classpath:datasets/matchsDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/matchsDataset.xml", type = DELETE_ALL)
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
        assertEquals(OffsetDateTime.of(dateMatch, ZoneId.systemDefault().getRules().getOffset(dateMatch)), match.getDateMatch());
        assertEquals(8, match.getNbJoueursMin().intValue());
        assertEquals(12, match.getNbJoueursMax().intValue());
        assertNotNull(match.getCreateur());
        assertEquals("Lionel", match.getCreateur().getFirstName());
        assertEquals("Messi", match.getCreateur().getSurname());
    }

    @Test
    void chercherMatchsAvecInscriptions() throws Exception {
        // Given
        String code = "QWERTY-1234";

        // When
        Match match = matchDAO.findMatchByCode(code);

        // Then
        assertNotNull(match);
        assertNotNull(match.getInscriptions());
        assertEquals(2, match.getInscriptions().size());

        for (Inscription ins : match.getInscriptions()) {
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
        assertNotNull(match.getInscriptions());
        assertTrue(match.getInscriptions().isEmpty());
    }

    @Test
    void findMatchesByPlayer() throws Exception {
        // Given
        Player player1 = new Player(1);
        LocalDateTime date = LocalDate.of(2018, 10, 2).atStartOfDay();
        OffsetDateTime dateInitiale = OffsetDateTime.of(date, ZoneId.systemDefault().getRules().getOffset(date));

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(player1, dateInitiale);

        // Then
        assertNotNull(matchs);
        assertEquals(1 ,matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("AZERTY-1234", matchs.get(0).getCode());
        assertTrue(matchs.get(0).getDateMatch().isAfter(dateInitiale));
        assertNotNull(matchs.get(0).getCreateur());
        assertEquals("Cristiano", matchs.get(0).getCreateur().getFirstName());
        assertEquals("Ronaldo", matchs.get(0).getCreateur().getSurname());
        assertEquals("cr7@email.com", matchs.get(0).getCreateur().getEmail());
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
        assertNotNull(match.getNbJoueursMin());
        assertNotNull(match.getNbJoueursInscrits());
        assertEquals(1, match.getSite().getId().intValue());
    }

    @Test
    void saveMatch() throws Exception {
        // Given
        OffsetDateTime maintenant = OffsetDateTime.now();
        Player player = playerDAO.findPlayerById(1);
        Site site = siteDAO.chercherSiteParId(1);

        Match match = new Match();
        match.setCode("C-" + (Instant.now().toEpochMilli() / 1000));
        match.setDateMatch(maintenant);
        match.setNbJoueursMin(10);
        match.setNbJoueursMax(12);
        match.setCovoiturageActif(true);
        match.setPartageActif(false);
        match.setCreateur(player);
        match.setDescription("Match de test");
        match.setSite(site);

        // When
        matchDAO.saveMatch(match);

        // Then
        assertNotNull(match.getId());
        assertTrue(match.getId() > 0);
    }

    @Test
    void registerPlayerSansVoiture() throws Exception {
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
        Player unregisteredPlayer = playerDAO.findPlayerById(2);
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
        OffsetDateTime dateTime = OffsetDateTime.now().minusYears(5L); // Il y a 5 ans

        // When
        int nbLignes = matchDAO.unregisterPlayerFromAllMatches(player);
        List<Match> matchsJoueur = matchDAO.findMatchesByPlayer(player, dateTime);

        // Then
        assertEquals(1, nbLignes);
        assertNotNull(matchsJoueur);
        assertTrue(matchsJoueur.isEmpty());
    }

    @Test
    void notifyPlayerRegistry() throws Exception {
        // Given
        Match match = matchDAO.findMatchById(1);
        assertEquals(1, match.getNbJoueursInscrits().intValue());

        // When
        int nbMatchs = matchDAO.notifyPlayerRegistry(match);
        match = matchDAO.findMatchById(1);

        // Then
        assertEquals(1, nbMatchs);
        assertEquals(2, match.getNbJoueursInscrits().intValue());
    }
}