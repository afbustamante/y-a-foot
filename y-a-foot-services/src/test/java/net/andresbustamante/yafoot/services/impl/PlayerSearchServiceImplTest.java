package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerSearchServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private PlayerSearchServiceImpl rechercheJoueursService;

    @Mock
    private PlayerDAO playerDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findInvalidPlayer() throws Exception {
        // Given
        String email = "test@email.com";
        UserContext ctx = new UserContext();

        // Then
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(null);
        Player player = rechercheJoueursService.findPlayerByEmail(email);

        // When
        assertNull(player);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
    }

    @Test
    void findValidPlayer() throws Exception {
        // Given
        String email = "test@email.com";
        Player player = new Player(1);
        UserContext ctx = new UserContext();

        // Then
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        Player existingPlayer = rechercheJoueursService.findPlayerByEmail(email);

        // When
        assertNotNull(existingPlayer);
        assertEquals(player, existingPlayer);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
    }
}