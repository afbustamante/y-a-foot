package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchSearchServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private MatchSearchServiceImpl matchSearchService;

    @Mock
    private MatchDao matchDAO;

    @Test
    void findExistingMatchByCode() throws Exception {
        // Given
        String code = "code";
        Match match = new Match(1);
        match.setCode(code);

        // When
        when(matchDAO.findMatchByCode(anyString())).thenReturn(match);
        Match existingMatch = matchSearchService.findMatchByCode(code);

        // Then
        assertNotNull(existingMatch);
        assertEquals(code, existingMatch.getCode());
        verify(matchDAO, times(1)).findMatchByCode(anyString());
    }

    @Test
    void findMatchByCodeUsingEmptyCode() throws Exception {
        // Given
        // When
        Match match = matchSearchService.findMatchByCode(null);

        // Then
        assertNull(match);
        verify(matchDAO, times(0)).findMatchByCode(anyString());
    }

    @Test
    void findMatchByCodeUsingInvalidCode() throws Exception {
        // Given
        String code = "code";

        // When
        when(matchDAO.findMatchByCode(anyString())).thenReturn(null);
        Match existingMatch = matchSearchService.findMatchByCode(code);

        // Then
        assertNull(existingMatch);
        verify(matchDAO, times(1)).findMatchByCode(anyString());
    }

    @Test
    void findMatchesByPlayer() throws Exception {
        // Given
        Match match1 = new Match(1);
        Match match2 = new Match(2);
        Player player1 = new Player(1);
        UserContext ctx = new UserContext();
        ctx.setTimezone(ZoneId.of("UTC"));

        // When
        when(matchDAO.findMatchesByPlayer(any(Player.class), eq(null), eq(null), any(), any()))
                .thenReturn(Arrays.asList(match1, match2));
        List<Match> matches = matchSearchService.findMatchesByPlayer(player1, null, null, null, LocalDate.now());

        // Then
        assertNotNull(matches);
        assertEquals(2, matches.size());
        verify(matchDAO).findMatchesByPlayer(any(Player.class), eq(null), eq(null), any(), any());
    }

    @Test
    void findMatchesByPlayerWithNoPlayerId() throws Exception {
        // Given
        UserContext ctx = new UserContext();
        ctx.setTimezone(ZoneId.systemDefault());

        // When
        List<Match> matches = matchSearchService.findMatchesByPlayer(null, null, null, null, null);

        // Then
        assertNotNull(matches);
        assertTrue(matches.isEmpty());
        verify(matchDAO, never()).findMatchesByPlayer(any(Player.class), eq(null), eq(null), eq(null), eq(null));
    }
}
