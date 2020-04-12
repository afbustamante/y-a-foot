package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.MatchManagementService;
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
    private MatchManagementService matchManagementService;

    @Mock
    private CarManagementService carManagementService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void registerNewPlayer() throws Exception {
        // Given
        Player player = new Player();
        player.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.isPlayerAlreadySignedIn(anyString())).thenReturn(false);
        gestionJoueursService.savePlayer(player, ctx);

        // Then
        verify(playerDAO, times(1)).isPlayerAlreadySignedIn(anyString());
        verify(userDAO, times(1)).saveUser(any(), any());
        verify(playerDAO, times(1)).savePlayer(any());
    }

    @Test
    void registerPlayerAlreadyRegistered() throws Exception {
        // Given
        Player player = new Player();
        player.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.isPlayerAlreadySignedIn(anyString())).thenReturn(true);
        assertThrows(ApplicationException.class, () -> gestionJoueursService.savePlayer(player, ctx));

        // Then
        verify(playerDAO, times(1)).isPlayerAlreadySignedIn(anyString());
        verify(userDAO, times(0)).saveUser(any(), any());
        verify(playerDAO, times(0)).savePlayer(any());
    }

    @Test
    void updateExistingPlayer() throws Exception {
        // Given
        Player existingPlayer = new Player(1);
        existingPlayer.setSurname("Smith");
        existingPlayer.setFirstName("Alan");
        existingPlayer.setPhoneNumber("0123456789");
        existingPlayer.setEmail("test@email.com");

        Player updatedPlayer = new Player(1);
        updatedPlayer.setSurname("Doe");
        updatedPlayer.setFirstName("John");
        updatedPlayer.setPhoneNumber("0423456789");
        updatedPlayer.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(existingPlayer);
        boolean succes = gestionJoueursService.updatePlayer(updatedPlayer, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerByEmail(any());
        verify(userDAO, times(1)).updateUser(any());
        verify(playerDAO, times(1)).updatePlayer(any());
        assertTrue(succes);
    }

    @Test
    void updatePasswordExistingPlayer() throws Exception {
        // Given
        Player existingPlayer = new Player(1);
        existingPlayer.setEmail("test@email.com");
        existingPlayer.setPassword("QWERTY123");

        Player updatedPlayer = new Player(1);
        updatedPlayer.setEmail("test@email.com");
        updatedPlayer.setPassword("AZERTY123");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(existingPlayer);
        boolean succes = gestionJoueursService.updatePlayer(updatedPlayer, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerByEmail(any());
        verify(userDAO, times(1)).updateUser(any());
        verify(playerDAO, times(1)).updatePlayer(any());
        assertTrue(succes);
    }

    @Test
    void updateInvalidPlayer() throws Exception {
        // Given
        Player updatedPlayer = new Player(1);
        updatedPlayer.setSurname("Doe");
        updatedPlayer.setFirstName("John");
        updatedPlayer.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(null);
        boolean succes = gestionJoueursService.updatePlayer(updatedPlayer, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerByEmail(any());
        verify(userDAO, times(0)).updateUser(any());
        verify(playerDAO, times(0)).updatePlayer(any());
        assertFalse(succes);
    }

    @Test
    void deactivateExistingPlayer() throws Exception {
        // Given
        Player player1 = new Player(1);
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player1);
        when(playerDAO.deactivatePlayer(any())).thenReturn(1);

        gestionJoueursService.deactivatePlayer(1, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerById(anyInt());
        verify(playerDAO, times(1)).deactivatePlayer(any());
    }

    @Test
    void deactivateInvalidPlayer() throws Exception {
        // Given
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(null);

        gestionJoueursService.deactivatePlayer(-1, ctx);

        // Then
        verify(playerDAO, times(1)).findPlayerById(anyInt());
        verify(playerDAO, times(0)).deactivatePlayer(any());
    }
}