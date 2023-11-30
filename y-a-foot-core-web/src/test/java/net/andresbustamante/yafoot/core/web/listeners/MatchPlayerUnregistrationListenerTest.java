package net.andresbustamante.yafoot.core.web.listeners;

import net.andresbustamante.yafoot.core.events.MatchPlayerUnsubscriptionEvent;
import net.andresbustamante.yafoot.core.services.MatchAlertingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchPlayerUnregistrationListenerTest {

    @InjectMocks
    private MatchPlayerUnregistrationListener matchPlayerUnregistrationListener;

    @Mock
    private MatchAlertingService matchAlertingService;

    @Test
    void testOnMessage() throws Exception {
        // Given
        MatchPlayerUnsubscriptionEvent event = new MatchPlayerUnsubscriptionEvent();
        event.setMatchId(1);
        event.setMatchCode("ABC");
        event.setPlayerId(2);
        event.setPlayerFirstName("Tom");

        // When
        assertDoesNotThrow(() -> matchPlayerUnregistrationListener.onMessage(event));

        // Then
        verify(matchAlertingService).checkForAlertsAfterPlayerRemovedFromMatch(1, 2);
    }
}
