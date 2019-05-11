package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RechercheJoueursServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private RechercheJoueursServiceImpl rechercheJoueursService;

    @Mock
    private JoueurDAO joueurDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void chercherJoueurInexistant() throws Exception {
        // Given
        String email = "test@email.com";
        Contexte ctx = new Contexte();

        // Then
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(null);
        Joueur joueur = rechercheJoueursService.chercherJoueur(email, ctx);

        // When
        assertNull(joueur);
        verify(joueurDAO, times(1)).chercherJoueurParEmail(anyString());
    }

    @Test
    public void chercherJoueurExistant() throws Exception {
        // Given
        String email = "test@email.com";
        Joueur joueur = new Joueur(1);
        Contexte ctx = new Contexte();

        // Then
        when(joueurDAO.chercherJoueurParEmail(anyString())).thenReturn(joueur);
        Joueur joueurExistant = rechercheJoueursService.chercherJoueur(email, ctx);

        // When
        assertNotNull(joueurExistant);
        assertEquals(joueur, joueurExistant);
        verify(joueurDAO, times(1)).chercherJoueurParEmail(anyString());
    }
}