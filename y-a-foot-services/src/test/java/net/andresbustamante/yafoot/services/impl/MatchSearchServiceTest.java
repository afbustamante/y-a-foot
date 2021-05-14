package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchSearchServiceTest extends AbstractServiceTest {

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
        Match existingMatch = matchSearchService.findMatchByCode(code);

        // Then
        assertNotNull(existingMatch);
        assertEquals(code, existingMatch.getCode());
        verify(matchDAO, times(1)).findMatchByCode(anyString());
    }

    @Test
    void findMatchByCodeUsingEmptyCode() throws Exception {
        // Given
        UserContext ctx = new UserContext();

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
        UserContext ctx = new UserContext();

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
        when(matchDAO.findMatchesByPlayer(any(Player.class), any(), any())).thenReturn(Arrays.asList(match1, match2));
        List<Match> matches = matchSearchService.findMatchesByPlayer(player1, null, LocalDate.now());

        // Then
        assertNotNull(matches);
        assertEquals(2, matches.size());
        verify(matchDAO).findMatchesByPlayer(any(Player.class), any(), any());
    }

    @Test
    void findMatchesByPlayerWithNoPlayerId() throws Exception {
        // Given
        UserContext ctx = new UserContext();
        ctx.setTimezone(ZoneId.systemDefault());

        // When
        List<Match> matches = matchSearchService.findMatchesByPlayer(null, null, null);

        // Then
        assertNotNull(matches);
        assertTrue(matches.isEmpty());
        verify(matchDAO, never()).findMatchesByPlayer(any(Player.class), any(), any());
    }
}