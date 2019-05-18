package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class GestionJoueursServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private GestionJoueursServiceImpl gestionJoueursService;

    @Mock
    private JoueurDAO joueurDAO;

    @Mock
    private UtilisateurDAO utilisateurDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void inscrireNouveauJoueur() throws Exception {
        // Given
        Joueur joueur = new Joueur();
        joueur.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.isJoueurInscrit(anyString())).thenReturn(false);
        boolean succes = gestionJoueursService.inscrireJoueur(joueur, ctx);

        // Then
        verify(joueurDAO, times(1)).isJoueurInscrit(anyString());
        verify(utilisateurDAO, times(1)).creerUtilisateur(any(), any());
        verify(joueurDAO, times(1)).creerJoueur(any());
        assertTrue(succes);
    }

    @Test
    public void inscrireJoueurDejaInscrit() throws Exception {
        // Given
        Joueur joueur = new Joueur();
        joueur.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.isJoueurInscrit(anyString())).thenReturn(true);
        boolean succes = gestionJoueursService.inscrireJoueur(joueur, ctx);

        // Then
        verify(joueurDAO, times(1)).isJoueurInscrit(anyString());
        verify(utilisateurDAO, times(0)).creerUtilisateur(any(), any());
        verify(joueurDAO, times(0)).creerJoueur(any());
        assertFalse(succes);
    }

    @Test
    public void actualiserJoueurExistant() throws Exception {
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
        boolean succes = gestionJoueursService.actualiserJoueur(joueurMaj, ctx);

        // Then
        verify(joueurDAO, times(1)).chercherJoueurParEmail(any());
        verify(utilisateurDAO, times(1)).actualiserUtilisateur(any());
        verify(joueurDAO, times(1)).actualiserJoueur(any());
        assertTrue(succes);
    }

    @Test
    public void actualiserMdpJoueurExistant() throws Exception {
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
        boolean succes = gestionJoueursService.actualiserJoueur(joueurMaj, ctx);

        // Then
        verify(joueurDAO, times(1)).chercherJoueurParEmail(any());
        verify(utilisateurDAO, times(1)).actualiserUtilisateur(any());
        verify(joueurDAO, times(1)).actualiserJoueur(any());
        assertTrue(succes);
    }

    @Test
    public void actualiserJoueurInexistant() throws Exception {
        // Given
        Joueur joueurMaj = new Joueur(1);
        joueurMaj.setNom("Doe");
        joueurMaj.setPrenom("John");
        joueurMaj.setEmail("test@email.com");
        Contexte ctx = new Contexte();

        // When
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(null);
        boolean succes = gestionJoueursService.actualiserJoueur(joueurMaj, ctx);

        // Then
        verify(joueurDAO, times(1)).chercherJoueurParEmail(any());
        verify(utilisateurDAO, times(0)).actualiserUtilisateur(any());
        verify(joueurDAO, times(0)).actualiserJoueur(any());
        assertFalse(succes);
    }
}