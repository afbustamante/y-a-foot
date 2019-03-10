package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MatchDAOTest extends AbstractDAOTest {

    private static final Joueur JOHN_DOE = new Joueur(null, "Doe", "John", "john.doe@email.com", "01234656789");
    private static final Site SITE = new Site("Foot", "123 Rue Fausse", "1234567890", null);

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private JoueurDAO joueurDAO;

    @Autowired
    private SiteDAO siteDAO;

    @Before
    public void setUp() throws Exception {
        // Insérer un joueur de test
        joueurDAO.creerJoueur(JOHN_DOE);

        // Insérer un site de test
        siteDAO.creerSite(SITE);
    }

    @After
    public void clean() throws Exception {
        // Supprimer les joueurs créés lors de l'exécution du test
        joueurDAO.supprimerJoueur(JOHN_DOE);
    }

    @Test
    public void isCodeExistant() throws Exception {
    }

    @Test
    public void chercherMatchParCode() throws Exception {
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
        Date maintenant = Calendar.getInstance().getTime();

        Match match = new Match();
        match.setCode("C-" + (Instant.now().toEpochMilli() / 1000));
        match.setDateMatch(maintenant);
        match.setNumJoueursMin(10);
        match.setNumJoueursMax(12);
        match.setCovoiturageActif(true);
        match.setPartageActif(false);
        match.setCreateur(JOHN_DOE);
        match.setDescription("Match de test");
        match.setSite(SITE);

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