package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.core.dao.PlayerDAO;
import net.andresbustamante.yafoot.core.model.Player;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerSearchServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PlayerSearchServiceImpl playerSearchService;

    @Mock
    private PlayerDAO playerDAO;

    @Test
    void findInvalidPlayerByEmail() throws Exception {
        // Given
        String email = "test@email.com";

        // Then
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(null);
        Player player = playerSearchService.findPlayerByEmail(email);

        // When
        assertNull(player);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
    }

    @Test
    void findValidPlayerByEmail() throws Exception {
        // Given
        String email = "test@email.com";
        Player player = new Player(1);

        // Then
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        Player existingPlayer = playerSearchService.findPlayerByEmail(email);

        // When
        assertNotNull(existingPlayer);
        assertEquals(player, existingPlayer);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
    }

    @Test
    void findInvalidPlayerById() throws Exception {
        // Given
        Integer id = 999;

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(null);

        Player player = playerSearchService.findPlayerById(id);

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

        assertNotNull(player);
        verify(playerDAO).findPlayerById(anyInt());
    }
}