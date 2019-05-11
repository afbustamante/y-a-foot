package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RechercheMatchsServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private RechercheMatchsServiceImpl rechercheMatchsService;

    @Mock
    private MatchDAO matchDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void chercherMatchExistantParCode() throws Exception {
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
    public void chercherMatchInexistantParCode() throws Exception {
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
    public void chercherMatchsJoueur() throws Exception {
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
    }
}