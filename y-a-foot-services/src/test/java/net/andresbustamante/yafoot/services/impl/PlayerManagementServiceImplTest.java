package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerManagementServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private PlayerManagementServiceImpl gestionJoueursService;

    @Mock
    private JoueurDAO joueurDAO;

    @Mock
    private UtilisateurDAO utilisateurDAO;

    @Mock
    private MatchDAO matchDAO;

    @Mock
    private VoitureDAO voitureDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void inscrireNouveauJoueur() throws Exception {
        // Given
        Joueur joueur = new Joueur();
        joueur.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.isJoueurInscrit(anyString())).thenReturn(false);
        boolean succes = gestionJoueursService.savePlayer(joueur, ctx);

        // Then
        verify(joueurDAO, times(1)).isJoueurInscrit(anyString());
        verify(utilisateurDAO, times(1)).creerUtilisateur(any(), any());
        verify(joueurDAO, times(1)).creerJoueur(any());
        assertTrue(succes);
    }

    @Test
    void inscrireJoueurDejaInscrit() throws Exception {
        // Given
        Joueur joueur = new Joueur();
        joueur.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.isJoueurInscrit(anyString())).thenReturn(true);
        boolean succes = gestionJoueursService.savePlayer(joueur, ctx);

        // Then
        verify(joueurDAO, times(1)).isJoueurInscrit(anyString());
        verify(utilisateurDAO, times(0)).creerUtilisateur(any(), any());
        verify(joueurDAO, times(0)).creerJoueur(any());
        assertFalse(succes);
    }

    @Test
    void actualiserJoueurExistant() throws Exception {
        // Given
        Joueur joueurExistant = new Joueur(1);
        joueurExistant.setNom("Smith");
        joueurExistant.setPrenom("Alan");
        joueurExistant.setTelephone("0123456789");
        joueurExistant.setEmail("test@email.com");

        Joueur joueurMaj = new Joueur(1);
        joueurMaj.setNom("Doe");
        joueurMaj.setPrenom("John");
        joueurMaj.setTelephone("0423456789");
        joueurMaj.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(joueurExistant);
        boolean succes = gestionJoueursService.updatePlayer(joueurMaj, ctx);

        // Then
        verify(joueurDAO, times(1)).chercherJoueurParEmail(any());
        verify(utilisateurDAO, times(1)).actualiserUtilisateur(any());
        verify(joueurDAO, times(1)).actualiserJoueur(any());
        assertTrue(succes);
    }

    @Test
    void actualiserMdpJoueurExistant() throws Exception {
        // Given
        Joueur joueurExistant = new Joueur(1);
        joueurExistant.setEmail("test@email.com");
        joueurExistant.setMotDePasse("QWERTY123");

        Joueur joueurMaj = new Joueur(1);
        joueurMaj.setEmail("test@email.com");
        joueurMaj.setMotDePasse("AZERTY123");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(joueurExistant);
        boolean succes = gestionJoueursService.updatePlayer(joueurMaj, ctx);

        // Then
        verify(joueurDAO, times(1)).chercherJoueurParEmail(any());
        verify(utilisateurDAO, times(1)).actualiserUtilisateur(any());
        verify(joueurDAO, times(1)).actualiserJoueur(any());
        assertTrue(succes);
    }

    @Test
    void actualiserJoueurInexistant() throws Exception {
        // Given
        Joueur joueurMaj = new Joueur(1);
        joueurMaj.setNom("Doe");
        joueurMaj.setPrenom("John");
        joueurMaj.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(null);
        boolean succes = gestionJoueursService.updatePlayer(joueurMaj, ctx);

        // Then
        verify(joueurDAO, times(1)).chercherJoueurParEmail(any());
        verify(utilisateurDAO, times(0)).actualiserUtilisateur(any());
        verify(joueurDAO, times(0)).actualiserJoueur(any());
        assertFalse(succes);
    }

    @Test
    void desactiverJoueurExistant() throws Exception {
        // Given
        Joueur joueur1 = new Joueur(1);
        String emailJoueur = "playerNumber1@email.com";
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(joueur1);
        when(matchDAO.desinscrireJoueur(any())).thenReturn(15);
        when(voitureDAO.supprimerVoitures(any())).thenReturn(1);
        when(joueurDAO.desactiverJoueur(any())).thenReturn(1);

        boolean succes = gestionJoueursService.deactivatePlayer(emailJoueur, ctx);

        // Then
        assertTrue(succes);
        verify(joueurDAO, times(1)).chercherJoueurParEmail(anyString());
        verify(matchDAO, times(1)).desinscrireJoueur(any());
        verify(voitureDAO, times(1)).supprimerVoitures(any());
        verify(joueurDAO, times(1)).desactiverJoueur(any());
    }

    @Test
    void desactiverJoueurInexistant() throws Exception {
        // Given
        Joueur autreJoueur = new Joueur(-1);
        String emailJoueur = "playerNumberX@email.com";
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(null);

        boolean succes = gestionJoueursService.deactivatePlayer(emailJoueur, ctx);

        // Then
        assertFalse(succes);
        verify(joueurDAO, times(1)).chercherJoueurParEmail(anyString());
        verify(matchDAO, times(0)).desinscrireJoueur(any());
        verify(voitureDAO, times(0)).supprimerVoitures(any());
        verify(joueurDAO, times(0)).desactiverJoueur(any());
    }
}