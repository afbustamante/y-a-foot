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
class JoueurDAOTest extends AbstractDAOTest {

    private static final String EMAIL = "john.doe@email.com";
    private static final String AUTRE_TELEPHONE = "0423456789";
    private static final String AUTRE_NOM = "Smith";
    private static final String AUTRE_PRENOM = "Alan";
    private static final String NOUVEL_EMAIL = "nonInscrit@email.com";

    private static final Joueur JOHN_DOE = new Joueur(1, "Doe", "John", EMAIL, "01234656789");

    @Autowired
    private JoueurDAO joueurDAO;

    @Test
    void creerJoueur() throws Exception {
        Joueur joueur = getNouveauJoueur();
        joueurDAO.creerJoueur(joueur);

        // Vérifier que le joueur à un identifiant de base de données attribué
        assertNotNull(joueur.getId());
        assertTrue(joueur.getId() > 0);
    }

    @Test
    void isJoueurInscrit() throws Exception {
        boolean isInscrit = joueurDAO.isJoueurInscrit(NOUVEL_EMAIL);
        assertFalse(isInscrit);

        isInscrit = joueurDAO.isJoueurInscrit(EMAIL);
        assertTrue(isInscrit);
    }

    @Test
    void chercherJoueurParId() throws Exception {
        Joueur joueur1 = joueurDAO.chercherJoueurParId(JOHN_DOE.getId());
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
    void chercherJoueurParEmail() throws Exception {
        Joueur joueur = joueurDAO.chercherJoueurParEmail(EMAIL);
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
    void actualiserJoueur() throws Exception {
        // Modifier les informations pour le joueur avec l'ID de John Doe
        Joueur joueur = joueurDAO.chercherJoueurParEmail(EMAIL);
        joueur.setTelephone(AUTRE_TELEPHONE);
        joueur.setNom(AUTRE_NOM);
        joueur.setPrenom(AUTRE_PRENOM);
        assertNull(joueur.getDateDerniereMaj());

        joueurDAO.actualiserJoueur(joueur);

        Joueur joueur1 = joueurDAO.chercherJoueurParEmail(EMAIL);

        // Les informations du joueur 1 doivent être modifiées
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
    void supprimerJoueur() throws Exception {
        // Chercher le joueur à supprimer
        joueurDAO.supprimerJoueur(JOHN_DOE);

        Joueur joueur = joueurDAO.chercherJoueurParEmail(EMAIL);

        // Le joueur n'existe plus
        assertNull(joueur);
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