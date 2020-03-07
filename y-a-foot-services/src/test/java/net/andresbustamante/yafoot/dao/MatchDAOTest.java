package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.*;
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
        assertEquals(ZonedDateTime.of(dateMatch, ZoneId.systemDefault()), match.getDateMatch());
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
            assertNotNull(ins.getJoueur());
            assertNotNull(ins.getId());
        }
    }

    @Test
    void chercherMatchsSansInscriptions() throws Exception {
        // Given
        Integer idMatch = 3;

        // When
        Match match = matchDAO.findMatchById(idMatch);

        // Then
        assertNotNull(match);
        assertNotNull(match.getInscriptions());
        assertTrue(match.getInscriptions().isEmpty());
    }

    @Test
    void findMatchesByPlayer() throws Exception {
        // Given
        Integer idJoueur = 1;
        LocalDateTime date = LocalDate.of(2018, 10, 2).atStartOfDay();
        ZonedDateTime dateInitiale = ZonedDateTime.of(date, ZoneId.systemDefault());

        // When
        List<Match> matchs = matchDAO.findMatchesByPlayer(idJoueur, dateInitiale);

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
        Integer idMatch = 1;

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
        ZonedDateTime maintenant = ZonedDateTime.now();
        Joueur joueur = playerDAO.findPlayerById(1);
        Site site = siteDAO.chercherSiteParId(1);

        Match match = new Match();
        match.setCode("C-" + (Instant.now().toEpochMilli() / 1000));
        match.setDateMatch(maintenant);
        match.setNbJoueursMin(10);
        match.setNbJoueursMax(12);
        match.setCovoiturageActif(true);
        match.setPartageActif(false);
        match.setCreateur(joueur);
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
        Joueur joueur = playerDAO.findPlayerById(1);
        Match match = matchDAO.findMatchById(2);

        // When
        matchDAO.registerPlayer(joueur, match, null);

        // Then
        assertTrue(matchDAO.isPlayerRegistered(joueur, match));
    }

    @Test
    void isPlayerRegistered() throws Exception {
        // Given
        Joueur joueurInscrit = playerDAO.findPlayerById(1);
        Joueur joueurNonInscrit = playerDAO.findPlayerById(2);
        Match match = matchDAO.findMatchById(1);

        // When
        boolean test1 = matchDAO.isPlayerRegistered(joueurInscrit, match);
        boolean test2 = matchDAO.isPlayerRegistered(joueurNonInscrit, match);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        Joueur joueur = playerDAO.findPlayerById(1);
        Match match = matchDAO.findMatchById(1);
        assertTrue(matchDAO.isPlayerRegistered(joueur, match));

        // When
        matchDAO.unregisterPlayer(joueur, match);

        // Then
        assertFalse(matchDAO.isPlayerRegistered(joueur, match));
    }

    @Test
    void unregisterPlayerFromAllMatches() throws Exception {
        // Given
        Joueur joueur = playerDAO.findPlayerById(1);
        ZonedDateTime dateTime = ZonedDateTime.now().minusYears(5L); // Il y a 5 ans

        // When
        int nbLignes = matchDAO.unregisterPlayerFromAllMatches(joueur);
        List<Match> matchsJoueur = matchDAO.findMatchesByPlayer(joueur.getId(), dateTime);

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