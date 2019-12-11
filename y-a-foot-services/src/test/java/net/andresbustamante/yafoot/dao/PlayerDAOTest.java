package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Joueur;
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

    private static final Joueur JOHN_DOE = new Joueur(1, "Doe", "John", EMAIL, "01234656789");

    @Autowired
    private PlayerDAO playerDAO;

    @Test
    void savePlayer() throws Exception {
        Joueur joueur = getNouveauJoueur();
        int nbJoueurs = playerDAO.savePlayer(joueur);

        // Vérifier que le joueur à un identifiant de base de données attribué
        assertEquals(1, nbJoueurs);
        assertNotNull(joueur.getId());
        assertTrue(joueur.getId() > 0);
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
        Joueur joueur1 = playerDAO.findPlayerById(JOHN_DOE.getId());
        assertNotNull(joueur1);

        // Vérifier que les informations sont complètes
        assertNotNull(joueur1.getNom());
        assertEquals(JOHN_DOE.getNom(), joueur1.getNom());
        assertNotNull(joueur1.getPrenom());
        assertEquals(JOHN_DOE.getPrenom(), joueur1.getPrenom());
        assertNotNull(joueur1.getTelephone());
        assertNotNull(joueur1.getEmail());
        assertEquals(JOHN_DOE.getTelephone(), joueur1.getTelephone());
        assertNotNull(joueur1.getDateCreation());
        assertEquals(LocalDateTime.of(2019, 1, 2, 12, 34, 56), joueur1.getDateCreation());
    }

    @Test
    void findPlayerByEmail() throws Exception {
        Joueur joueur = playerDAO.findPlayerByEmail(EMAIL);
        assertNotNull(joueur);
        assertNotNull(joueur.getNom());
        assertEquals(JOHN_DOE.getNom(), joueur.getNom());
        assertNotNull(joueur.getPrenom());
        assertEquals(JOHN_DOE.getPrenom(), joueur.getPrenom());
        assertNotNull(joueur.getTelephone());
        assertEquals(JOHN_DOE.getTelephone(), joueur.getTelephone());
        assertNotNull(joueur.getEmail());
        assertEquals(JOHN_DOE.getEmail(), joueur.getEmail());
        assertNotNull(joueur.getDateCreation());
    }

    @Test
    void updatePlayer() throws Exception {
        // Modifier les informations pour le joueur avec l'ID de John Doe
        Joueur joueur = playerDAO.findPlayerByEmail(EMAIL);
        joueur.setTelephone(AUTRE_TELEPHONE);
        joueur.setNom(AUTRE_NOM);
        joueur.setPrenom(AUTRE_PRENOM);
        assertNull(joueur.getDateDerniereMaj());

        int nbJoueurs = playerDAO.updatePlayer(joueur);

        Joueur joueur1 = playerDAO.findPlayerByEmail(EMAIL);

        // Les informations du joueur 1 doivent être modifiées
        assertEquals(1, nbJoueurs);
        assertNotNull(joueur1.getTelephone());
        assertEquals(AUTRE_TELEPHONE, joueur1.getTelephone());
        assertNotNull(joueur1.getNom());
        assertEquals(AUTRE_NOM, joueur1.getNom());
        assertNotNull(joueur1.getPrenom());
        assertEquals(AUTRE_PRENOM, joueur1.getPrenom());
        assertNotNull(joueur1.getDateDerniereMaj());
        assertTrue(joueur.getDateCreation().isBefore(joueur1.getDateDerniereMaj()));
    }

    @Test
    void deletePlayer() throws Exception {
        // When
        int nbJoueurs = playerDAO.deletePlayer(JOHN_DOE);
        Joueur joueur = playerDAO.findPlayerByEmail(EMAIL);

        // Then
        assertEquals(1, nbJoueurs);
        // Le joueur n'existe plus
        assertNull(joueur);
    }

    @Test
    void deactivatePlayer() throws Exception {
        // When
        int nbJoueurs = playerDAO.deactivatePlayer(JOHN_DOE);
        Joueur joueur = playerDAO.findPlayerById(JOHN_DOE.getId());

        // Then
        assertEquals(1, nbJoueurs);
        assertNotNull(joueur);
        assertTrue(joueur.getPrenom().startsWith("User"));
        assertTrue(joueur.getPrenom().endsWith(joueur.getId().toString()));
        assertEquals("Foot", joueur.getNom());
        assertFalse(joueur.isActif());
        assertNull(joueur.getTelephone());
        assertTrue(joueur.getEmail().startsWith(JOHN_DOE.getEmail()));
        assertNotEquals(JOHN_DOE.getEmail(), joueur.getEmail());
        assertTrue(joueur.getEmail().endsWith(".old"));
    }

    /**
     * Créer un nouveau joueur temporaire
     *
     * @return Nouveau joueur avec les informations de base
     */
    private Joueur getNouveauJoueur() {
        Joueur joueur = new Joueur();
        joueur.setEmail("hope.solo@test.com");
        joueur.setPrenom("Hope");
        joueur.setNom("Solo");
        joueur.setTelephone("0122334455");
        return joueur;
    }
}