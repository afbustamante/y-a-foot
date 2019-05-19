package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RechercheMatchsServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private RechercheMatchsServiceImpl rechercheMatchsService;

    @Mock
    private MatchDAO matchDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void chercherMatchExistantParCode() throws Exception {
        // Given
        String code = "code";
        Contexte ctx = new Contexte();
        Match match = new Match(1);
        match.setCode(code);

        // When
        when(matchDAO.chercherMatchParCode(anyString())).thenReturn(match);
        Match matchExistant = rechercheMatchsService.chercherMatchParCode(code, ctx);

        // Then
        assertNotNull(matchExistant);
        assertEquals(code, matchExistant.getCode());
        verify(matchDAO, times(1)).chercherMatchParCode(anyString());
    }

    @Test
    void chercherMatchParCodeVide() throws Exception {
        // Given
        Contexte ctx = new Contexte();

        // When
        Match match = rechercheMatchsService.chercherMatchParCode(null, ctx);

        // Then
        assertNull(match);
        verify(matchDAO, times(0)).chercherMatchParCode(anyString());
    }

    @Test
    void chercherMatchInexistantParCode() throws Exception {
        // Given
        String code = "code";
        Contexte ctx = new Contexte();

        // When
        when(matchDAO.chercherMatchParCode(anyString())).thenReturn(null);
        Match matchExistant = rechercheMatchsService.chercherMatchParCode(code, ctx);

        // Then
        assertNull(matchExistant);
        verify(matchDAO, times(1)).chercherMatchParCode(anyString());
    }

    @Test
    void chercherMatchsJoueur() throws Exception {
        // Given
        Match match1 = new Match(1);
        Match match2 = new Match(2);
        Integer idJoueur = 1;
        Contexte ctx = new Contexte();
        ctx.setTimeZone(ZoneId.of("UTC"));

        // When
        when(matchDAO.chercherMatchsParJoueur(anyInt(), any())).thenReturn(Arrays.asList(match1, match2));
        List<Match> matchs = rechercheMatchsService.chercherMatchsJoueur(idJoueur, ctx);

        // Then
        assertNotNull(matchs);
        assertEquals(2, matchs.size());
        verify(matchDAO, times(1)).chercherMatchsParJoueur(anyInt(), any());
    }

    @Test
    void chercherMatchsJoueurSansIdJoueur() throws Exception {
        // Given
        Contexte ctx = new Contexte();
        ctx.setTimeZone(ZoneId.of("UTC"));

        // When
        List<Match> matchs = rechercheMatchsService.chercherMatchsJoueur(null, ctx);

        // Then
        assertNotNull(matchs);
        assertTrue(matchs.isEmpty());
        verify(matchDAO, times(0)).chercherMatchsParJoueur(anyInt(), any());
    }
}