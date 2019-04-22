package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.Assert.*;

@DatabaseSetup(value = "classpath:datasets/matchsDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/matchsDataset.xml", type = DELETE_ALL)
public class MatchDAOTest extends AbstractDAOTest {

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private JoueurDAO joueurDAO;

    @Autowired
    private SiteDAO siteDAO;

    @Test
    public void isCodeExistant() throws Exception {
        // Given
        String codeExistant = "QWERTY-1234";
        String codeInexistant = "QWERTY-1230";

        // When
        boolean test1 = matchDAO.isCodeExistant(codeExistant);
        boolean test2 = matchDAO.isCodeExistant(codeInexistant);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    public void chercherMatchParCode() throws Exception {
        // Given
        String code = "QWERTY-1234";
        LocalDateTime dateMatch = LocalDateTime.of(2019, 10, 2, 19, 10); // 2019-10-02 19:10

        // When
        Match match = matchDAO.chercherMatchParCode(code);

        // Then
        assertNotNull(match);
        assertEquals(2, match.getId().intValue());
        assertEquals(ZonedDateTime.of(dateMatch, ZoneId.systemDefault()), match.getDateMatch());
        assertEquals(8, match.getNumJoueursMin().intValue());
        assertEquals(12, match.getNumJoueursMax().intValue());
        assertNotNull(match.getCreateur());
        assertEquals("Lionel", match.getCreateur().getPrenom());
        assertEquals("Messi", match.getCreateur().getNom());
    }

    @Test
    public void chercherMatchsAvecInscriptions() throws Exception {
        // Given
        String code = "QWERTY-1234";

        // When
        Match match = matchDAO.chercherMatchParCode(code);

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
    public void chercherMatchsSansInscriptions() throws Exception {
        // Given
        Integer idMatch = 3;

        // When
        Match match = matchDAO.chercherMatchParId(idMatch);

        // Then
        assertNotNull(match);
        assertNotNull(match.getInscriptions());
        assertTrue(match.getInscriptions().isEmpty());
    }

    @Test
    public void chercherMatchsParJoueur() throws Exception {
        // Given
        Integer idJoueur = 1;
        LocalDateTime date = LocalDate.of(2018, 10, 2).atStartOfDay();
        ZonedDateTime dateInitiale = ZonedDateTime.of(date, ZoneId.systemDefault());

        // When
        List<Match> matchs = matchDAO.chercherMatchsParJoueur(idJoueur, dateInitiale);

        // Then
        assertNotNull(matchs);
        assertEquals(1 ,matchs.size());
        assertNotNull(matchs.get(0));
        assertEquals("AZERTY-1234", matchs.get(0).getCode());
        assertTrue(matchs.get(0).getDateMatch().isAfter(dateInitiale));
        assertNotNull(matchs.get(0).getCreateur());
        assertEquals("Cristiano", matchs.get(0).getCreateur().getPrenom());
        assertEquals("Ronaldo", matchs.get(0).getCreateur().getNom());
        assertEquals("cr7@email.com", matchs.get(0).getCreateur().getEmail());
    }

    @Test
    public void chercherMatchParId() throws Exception {
        // Given
        Integer idMatch = 1;

        // When
        Match match = matchDAO.chercherMatchParId(1);

        //Then
        assertNotNull(match);
        assertEquals("AZERTY-1234", match.getCode());
        assertNotNull(match.getSite());
        assertEquals(1, match.getSite().getId().intValue());
    }

    @Test
    public void creerMatch() throws Exception {
        // Given
        ZonedDateTime maintenant = ZonedDateTime.now();
        Joueur joueur = joueurDAO.chercherJoueurParId(1);
        Site site = siteDAO.chercherSiteParId(1);

        Match match = new Match();
        match.setCode("C-" + (Instant.now().toEpochMilli() / 1000));
        match.setDateMatch(maintenant);
        match.setNumJoueursMin(10);
        match.setNumJoueursMax(12);
        match.setCovoiturageActif(true);
        match.setPartageActif(false);
        match.setCreateur(joueur);
        match.setDescription("Match de test");
        match.setSite(site);

        Utilisateur createur = new Utilisateur();
        createur.setId(1);

        // When
        matchDAO.creerMatch(match);

        // Then
        assertNotNull(match.getId());
        assertTrue(match.getId() > 0);
    }

    @Test
    public void inscrireJoueurMatchSansVoiture() throws Exception {
        // Given
        Joueur joueur = joueurDAO.chercherJoueurParId(1);
        Match match = matchDAO.chercherMatchParId(2);

        // When
        matchDAO.inscrireJoueurMatch(joueur, match, null);

        // Then
        assertTrue(matchDAO.isJoueurInscritMatch(joueur, match));
    }

    @Test
    public void isJoueurInscritMatch() throws Exception {
        // Given
        Joueur joueurInscrit = joueurDAO.chercherJoueurParId(1);
        Joueur joueurNonInscrit = joueurDAO.chercherJoueurParId(2);
        Match match = matchDAO.chercherMatchParId(1);

        // When
        boolean test1 = matchDAO.isJoueurInscritMatch(joueurInscrit, match);
        boolean test2 = matchDAO.isJoueurInscritMatch(joueurNonInscrit, match);

        // Then
        assertTrue(test1);
        assertFalse(test2);
    }

    @Test
    public void desinscrireJoueurMatch() throws Exception {
        // Given
        Joueur joueur = joueurDAO.chercherJoueurParId(1);
        Match match = matchDAO.chercherMatchParId(1);
        assertTrue(matchDAO.isJoueurInscritMatch(joueur, match));

        // When
        matchDAO.desinscrireJoueurMatch(joueur, match);

        // Then
        assertFalse(matchDAO.isJoueurInscritMatch(joueur, match));
    }
}