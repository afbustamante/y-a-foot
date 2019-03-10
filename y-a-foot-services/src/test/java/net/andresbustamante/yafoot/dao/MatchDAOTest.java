package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;

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
        String codeInexistant = "QWERTZ-1234";

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
    }

    @Test
    public void chercherMatchsParJoueur() throws Exception {
    }

    @Test
    public void chercherMatchParId() throws Exception {
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

        // When
        matchDAO.creerMatch(match);

        // Then
        assertNotNull(match.getId());
        assertTrue(match.getId() > 0);
    }

    @Test
    public void inscrireJoueurMatch() throws Exception {
    }

    @Test
    public void isJoueurInscritMatch() throws Exception {
    }
}