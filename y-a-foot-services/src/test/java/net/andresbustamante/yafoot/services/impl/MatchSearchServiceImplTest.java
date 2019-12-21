package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.model.UserContext;
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

class MatchSearchServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private MatchSearchServiceImpl matchSearchService;

    @Mock
    private MatchDAO matchDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findExistingMatchByCode() throws Exception {
        // Given
        String code = "code";
        UserContext ctx = new UserContext();
        Match match = new Match(1);
        match.setCode(code);

        // When
        when(matchDAO.findMatchByCode(anyString())).thenReturn(match);
        Match matchExistant = matchSearchService.findMatchByCode(code, ctx);

        // Then
        assertNotNull(matchExistant);
        assertEquals(code, matchExistant.getCode());
        verify(matchDAO, times(1)).findMatchByCode(anyString());
    }

    @Test
    void findMatchByCodeUsingEmptyCode() throws Exception {
        // Given
        UserContext ctx = new UserContext();

        // When
        Match match = matchSearchService.findMatchByCode(null, ctx);

        // Then
        assertNull(match);
        verify(matchDAO, times(0)).findMatchByCode(anyString());
    }

    @Test
    void findMatchByCodeUsingInvalidCode() throws Exception {
        // Given
        String code = "code";
        UserContext ctx = new UserContext();

        // When
        when(matchDAO.findMatchByCode(anyString())).thenReturn(null);
        Match matchExistant = matchSearchService.findMatchByCode(code, ctx);

        // Then
        assertNull(matchExistant);
        verify(matchDAO, times(1)).findMatchByCode(anyString());
    }

    @Test
    void findMatchesByPlayer() throws Exception {
        // Given
        Match match1 = new Match(1);
        Match match2 = new Match(2);
        Integer idJoueur = 1;
        UserContext ctx = new UserContext();
        ctx.setTimezone(ZoneId.of("UTC"));

        // When
        when(matchDAO.findMatchesByPlayer(anyInt(), any())).thenReturn(Arrays.asList(match1, match2));
        List<Match> matchs = matchSearchService.findMatchesByPlayer(idJoueur, ctx);

        // Then
        assertNotNull(matchs);
        assertEquals(2, matchs.size());
        verify(matchDAO, times(1)).findMatchesByPlayer(anyInt(), any());
    }

    @Test
    void findMatchesByPlayerWithNoPlayerId() throws Exception {
        // Given
        UserContext ctx = new UserContext();
        ctx.setTimezone(ZoneId.of("UTC"));

        // When
        List<Match> matchs = matchSearchService.findMatchesByPlayer(null, ctx);

        // Then
        assertNotNull(matchs);
        assertTrue(matchs.isEmpty());
        verify(matchDAO, times(0)).findMatchesByPlayer(anyInt(), any());
    }
}