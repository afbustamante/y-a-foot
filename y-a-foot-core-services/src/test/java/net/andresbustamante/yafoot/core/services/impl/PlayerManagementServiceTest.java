package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.core.dao.PlayerDAO;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.core.exceptions.PlayerNotFoundException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.auth.model.enums.RolesEnum;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.MatchManagementService;
import net.andresbustamante.yafoot.auth.services.UserManagementService;
import net.andresbustamante.yafoot.core.services.impl.PlayerManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerManagementServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PlayerManagementServiceImpl playerManagementService;

    @Mock
    private PlayerDAO playerDAO;

    @Mock
    private UserManagementService userManagementService;

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
        when(playerDAO.isPlayerAlreadySignedUp(anyString())).thenReturn(false);
        playerManagementService.savePlayer(player, ctx);

        // Then
        verify(playerDAO).isPlayerAlreadySignedUp(anyString());
        verify(userManagementService).createUser(any(User.class), any(RolesEnum.class), any(UserContext.class));
        verify(playerDAO).savePlayer(any());
    }

    @Test
    void registerPlayerAlreadyRegistered() throws Exception {
        // Given
        Player player = new Player();
        player.setEmail("test@email.com");
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.isPlayerAlreadySignedUp(anyString())).thenReturn(true);
        assertThrows(ApplicationException.class, () -> playerManagementService.savePlayer(player, ctx));

        // Then
        verify(playerDAO).isPlayerAlreadySignedUp(anyString());
        verify(userManagementService, never()).createUser(any(User.class), any(RolesEnum.class), any(UserContext.class));
        verify(playerDAO, never()).savePlayer(any());
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
        playerManagementService.updatePlayer(updatedPlayer, ctx);

        // Then
        verify(playerDAO).findPlayerByEmail(any());
        verify(userManagementService).updateUser(any(User.class), any(UserContext.class));
        verify(playerDAO).updatePlayer(any());
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
        assertThrows(PlayerNotFoundException.class, () -> playerManagementService.updatePlayer(updatedPlayer, ctx));

        // Then
        verify(playerDAO).findPlayerByEmail(any());
        verify(userManagementService, never()).updateUser(any(User.class), any(UserContext.class));
        verify(playerDAO, never()).updatePlayer(any());
    }

    @Test
    void deactivateExistingPlayer() throws Exception {
        // Given
        Player player1 = new Player(1);
        UserContext ctx = new UserContext();

        // When
        when(playerDAO.deactivatePlayer(any())).thenReturn(1);

        playerManagementService.deactivatePlayer(player1, ctx);

        // Then
        verify(playerDAO).deactivatePlayer(any());
    }
}