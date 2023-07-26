package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.commons.utils.TemplateUtils;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Registration;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link MatchAlertingServiceImpl}.
 *
 * @see net.andresbustamante.yafoot.core.services.MatchAlertingService
 */
class MatchAlertingServiceTest extends AbstractServiceUnitTest {

    @Mock
    private MatchDao matchDao;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TemplateUtils templateUtils;

    @Mock
    private MessagingService messagingService;

    @InjectMocks
    private MatchAlertingServiceImpl matchAlertingService;

    @Test
    void testCheckForAlertsAfterPlayerRemovedFromMatchThenNoMessageSent() throws Exception {
        // Given
        int matchId = 1;
        int playerId = 2;

        Match match = new Match(matchId);
        match.setCode("code");
        match.setDate(LocalDateTime.now().plusDays(1));

        when(matchDao.findMatchById(anyInt())).thenReturn(match);

        // When
        assertDoesNotThrow(() -> matchAlertingService.checkForAlertsAfterPlayerRemovedFromMatch(matchId, playerId));

        // Then
        verify(messagingService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testCheckForAlertsAfterPlayerRemovedFromMatchThenMessageSent() throws Exception {
        // Given
        int matchId = 1;
        int playerId = 2;

        Player matchCreator = new Player(5);
        matchCreator.setPreferredLanguage("fr");
        matchCreator.setEmail("createur@email.com");
        matchCreator.setFirstName("Luc");

        Match match = new Match(matchId);
        match.setCode("code");
        match.setDate(LocalDateTime.now().plusDays(1));
        match.setRegistrations(List.of(new Registration(matchId, 1), new Registration(matchId, 3)));
        match.setNumPlayersMin(2);
        match.setCreator(matchCreator);

        when(matchDao.findMatchById(anyInt())).thenReturn(match);
        when(templateUtils.getContent(anyString(), any())).thenReturn("content");
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("subject");

        // When
        assertDoesNotThrow(() -> matchAlertingService.checkForAlertsAfterPlayerRemovedFromMatch(matchId, playerId));

        // Then
        verify(messagingService).sendEmail(anyString(), eq("subject"), eq("content"));
    }
}
