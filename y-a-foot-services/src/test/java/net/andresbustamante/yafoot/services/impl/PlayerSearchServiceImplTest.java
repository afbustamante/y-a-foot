package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
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
        Joueur joueur = rechercheJoueursService.findPlayerByEmail(email, ctx);

        // When
        assertNull(joueur);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
    }

    @Test
    void findValidPlayer() throws Exception {
        // Given
        String email = "test@email.com";
        Joueur joueur = new Joueur(1);
        UserContext ctx = new UserContext();

        // Then
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        Joueur joueurExistant = rechercheJoueursService.findPlayerByEmail(email, ctx);

        // When
        assertNotNull(joueurExistant);
        assertEquals(joueur, joueurExistant);
        verify(playerDAO, times(1)).findPlayerByEmail(anyString());
    }
}