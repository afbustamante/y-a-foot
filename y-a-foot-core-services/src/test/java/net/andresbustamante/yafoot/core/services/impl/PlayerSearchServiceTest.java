package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.model.Player;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerSearchServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private PlayerSearchServiceImpl playerSearchService;

    @Mock
    private PlayerDao playerDAO;

    @Test
    void findInvalidPlayerByEmail() throws Exception {
        // Given
        String email = "test@email.com";
        UserContext userContext = new UserContext(email);

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(null);
        Player player = playerSearchService.findPlayerByEmail(email, userContext);

        // Then
        assertNull(player);
        verify(playerDAO).findPlayerByEmail(anyString());
    }

    @Test
    void findValidPlayerByEmail() throws Exception {
        // Given
        String email = "test@email.com";
        Player player = new Player(1);
        UserContext userContext = new UserContext(email);

        // When
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        Player existingPlayer = playerSearchService.findPlayerByEmail(email, userContext);

        // Then
        assertNotNull(existingPlayer);
        assertEquals(player, existingPlayer);
        verify(playerDAO).findPlayerByEmail(anyString());
    }

    @Test
    void findAnotherPlayerByEmail() throws Exception {
        // Given
        String email = "test@email.com";
        UserContext userContext = new UserContext("another@email.com");

        // When
        assertThrows(ApplicationException.class, () -> playerSearchService.findPlayerByEmail(email, userContext));

        // Then
        verify(playerDAO, never()).findPlayerByEmail(anyString());
    }

    @Test
    void findInvalidPlayerById() throws Exception {
        // Given
        Integer id = 999;

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(null);

        Player player = playerSearchService.findPlayerById(id);

        // Then
        assertNull(player);
        verify(playerDAO).findPlayerById(anyInt());
    }

    @Test
    void findValidPlayerById() throws Exception {
        // Given
        Integer id = 1;
        Player player1 = new Player(1);

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player1);

        Player player = playerSearchService.findPlayerById(id);

        // Then
        assertNotNull(player);
        verify(playerDAO).findPlayerById(anyInt());
    }
}
