package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class JoueurDAOTest extends AbstractDAOTest {

    private static final String EMAIL = "john.doe@email.com";
    private static final String AUTRE_TELEPHONE = "0423456789";
    private static final String AUTRE_NOM = "Smith";
    private static final String AUTRE_PRENOM = "Alan";
    private static final String NOUVEL_EMAIL = "nonInscrit@email.com";

    private static final Joueur JOHN_DOE = new Joueur(null, "Doe", "John", EMAIL, "01234656789");

    @Autowired
    JoueurDAO joueurDAO;

    @Before
    public void setUp() throws Exception {
        // Insérer un joueur de test
        joueurDAO.creerJoueur(JOHN_DOE);
    }

    @After
    public void clean() throws Exception {
        // Supprimer les joueurs créés lors de l'exécution du test
        joueurDAO.supprimerJoueur(JOHN_DOE);
        joueurDAO.supprimerJoueur(getNouveauJoueur());
    }

    @Test
    public void creerJoueur() throws Exception {
        Joueur joueur = getNouveauJoueur();
        joueurDAO.creerJoueur(joueur);

        // Vérifier que le joueur à un identifiant de base de données attribué
        assertNotNull(joueur.getId());
        assertTrue(joueur.getId() > 0);

        joueurDAO.supprimerJoueur(joueur);
    }

    @Test
    public void isJoueurInscrit() throws Exception {
        boolean isInscrit = joueurDAO.isJoueurInscrit(NOUVEL_EMAIL);
        assertFalse(isInscrit);

        isInscrit = joueurDAO.isJoueurInscrit(EMAIL);
        assertTrue(isInscrit);
    }

    @Test
    public void chercherJoueurParId() throws Exception {
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
    }

    @Test
    public void chercherJoueurParEmail() throws Exception {
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
    public void actualiserJoueur() throws Exception {
        Joueur joueur1 = joueurDAO.chercherJoueurParEmail(EMAIL);
        assertNotNull(joueur1);

        // Copier les informations du joueur1
        String nom = joueur1.getNom();
        String prenom = joueur1.getPrenom();
        String telephone = joueur1.getTelephone();

        // Modifier les informations pour le joueur avec l'ID de John Doe
        Joueur joueur = new Joueur();
        joueur.setId(JOHN_DOE.getId());
        joueur.setTelephone(AUTRE_TELEPHONE);
        joueur.setNom(AUTRE_NOM);
        joueur.setPrenom(AUTRE_PRENOM);

        joueurDAO.actualiserJoueur(joueur);
        joueur1 = joueurDAO.chercherJoueurParEmail(EMAIL);

        // Les informations du joueur 1 doivent être modifiées
        assertNotNull(joueur1.getTelephone());
        assertEquals(AUTRE_TELEPHONE, joueur1.getTelephone());
        assertNotNull(joueur1.getNom());
        assertEquals(AUTRE_NOM, joueur1.getNom());
        assertNotNull(joueur1.getPrenom());
        assertEquals(AUTRE_PRENOM, joueur1.getPrenom());

        // Remettre les informations telles qu'elles étaient avant
        joueur1.setNom(nom);
        joueur1.setPrenom(prenom);
        joueur1.setTelephone(telephone);
        joueurDAO.actualiserJoueur(joueur1);
    }

    @Test
    public void supprimerJoueur() throws Exception {
        // Chercher le joueur à supprimer
        Joueur joueur = getNouveauJoueur();
        String email = joueur.getEmail();
        joueurDAO.creerJoueur(joueur);
        assertNotNull(joueur.getId());

        joueurDAO.supprimerJoueur(joueur);
        joueur = joueurDAO.chercherJoueurParEmail(email);

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