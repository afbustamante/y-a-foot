package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.UserContext;
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
    private PlayerDAO playerDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private MatchDAO matchDAO;

    @Mock
    private CarDAO carDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void registerNewPlayer() throws Exception {
        // Given
        Joueur joueur = new Joueur();
        joueur.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.isPlayerAlreadySignedIn(anyString())).thenReturn(false);
        boolean succes = gestionJoueursService.savePlayer(joueur, ctx);

        // Then
        verify(playerDAO, times(1)).isPlayerAlreadySignedIn(anyString());
        verify(userDAO, times(1)).saveUser(any(), any());
        verify(playerDAO, times(1)).savePlayer(any());
        assertTrue(succes);
    }

    @Test
    void registerPlayerAlreadyRegistered() throws Exception {
        // Given
        Joueur joueur = new Joueur();
        joueur.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.isPlayerAlreadySignedIn(anyString())).thenReturn(true);
        boolean succes = gestionJoueursService.savePlayer(joueur, ctx);

        // Then
        verify(playerDAO, times(1)).isPlayerAlreadySignedIn(anyString());
        verify(userDAO, times(0)).saveUser(any(), any());
        verify(playerDAO, times(0)).savePlayer(any());
        assertFalse(succes);
    }

    @Test
    void updateExistingPlayer() throws Exception {
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
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueurExistant);
        boolean succes = gestionJoueursService.updatePlayer(joueurMaj, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerByEmail(any());
        verify(userDAO, times(1)).updateUser(any());
        verify(playerDAO, times(1)).updatePlayer(any());
        assertTrue(succes);
    }

    @Test
    void updatePasswordExistingPlayer() throws Exception {
        // Given
        Joueur joueurExistant = new Joueur(1);
        joueurExistant.setEmail("test@email.com");
        joueurExistant.setMotDePasse("QWERTY123");

        Joueur joueurMaj = new Joueur(1);
        joueurMaj.setEmail("test@email.com");
        joueurMaj.setMotDePasse("AZERTY123");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueurExistant);
        boolean succes = gestionJoueursService.updatePlayer(joueurMaj, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerByEmail(any());
        verify(userDAO, times(1)).updateUser(any());
        verify(playerDAO, times(1)).updatePlayer(any());
        assertTrue(succes);
    }

    @Test
    void updateInvalidPlayer() throws Exception {
        // Given
        Joueur joueurMaj = new Joueur(1);
        joueurMaj.setNom("Doe");
        joueurMaj.setPrenom("John");
        joueurMaj.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(null);
        boolean succes = gestionJoueursService.updatePlayer(joueurMaj, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerByEmail(any());
        verify(userDAO, times(0)).updateUser(any());
        verify(playerDAO, times(0)).updatePlayer(any());
        assertFalse(succes);
    }

    @Test
    void deactivateExistingPlayer() throws Exception {
        // Given
        Joueur joueur1 = new Joueur(1);
        String emailJoueur = "playerNumber1@email.com";
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur1);
        when(matchDAO.unregisterPlayerFromAllMatches(any())).thenReturn(15);
        when(carDAO.deleteCarsForPlayer(any())).thenReturn(1);
        when(playerDAO.deactivatePlayer(any())).thenReturn(1);

        boolean succes = gestionJoueursService.deactivatePlayer(emailJoueur, ctx);

        // Then
        assertTrue(succes);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
        verify(matchDAO, times(1)).unregisterPlayerFromAllMatches(any());
        verify(carDAO, times(1)).deleteCarsForPlayer(any());
        verify(playerDAO, times(1)).deactivatePlayer(any());
    }

    @Test
    void deactivateInvalidPlayer() throws Exception {
        // Given
        Joueur autreJoueur = new Joueur(-1);
        String emailJoueur = "playerNumberX@email.com";
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(null);

        boolean succes = gestionJoueursService.deactivatePlayer(emailJoueur, ctx);

        // Then
        assertFalse(succes);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
        verify(matchDAO, times(0)).unregisterPlayerFromAllMatches(any());
        verify(carDAO, times(0)).deleteCarsForPlayer(any());
        verify(playerDAO, times(0)).deactivatePlayer(any());
    }
}