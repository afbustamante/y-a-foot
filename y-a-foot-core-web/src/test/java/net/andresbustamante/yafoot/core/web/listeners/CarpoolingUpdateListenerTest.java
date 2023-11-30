package net.andresbustamante.yafoot.core.web.listeners;

import net.andresbustamante.yafoot.core.events.CarpoolingUpdateEvent;
import net.andresbustamante.yafoot.core.services.CarpoolingNotificationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarpoolingUpdateListenerTest {

    @InjectMocks
    private CarpoolingUpdateListener carpoolingUpdateListener;

    @Mock
    private CarpoolingNotificationsService carpoolingNotificationsService;

    @Test
    void testOnMessageForAcceptedRequestEvent() throws Exception {
        // Given
        CarpoolingUpdateEvent event = new CarpoolingUpdateEvent();
        event.setMatchId(1);
        event.setMatchCode("ABC");
        event.setPlayerId(2);
        event.setPlayerFirstName("Tom");
        event.setCarId(3);
        event.setCarName("Jag");
        event.setCarSeatConfirmed(1);

        // When
        assertDoesNotThrow(() -> carpoolingUpdateListener.onMessage(event));

        // Then
        verify(carpoolingNotificationsService).notifyCarpoolingUpdate(2, 1, 3, true);
    }

    @Test
    void testOnMessageForRefusedRequestEvent() throws Exception {
        // Given
        CarpoolingUpdateEvent event = new CarpoolingUpdateEvent();
        event.setMatchId(1);
        event.setMatchCode("ABC");
        event.setPlayerId(2);
        event.setPlayerFirstName("Tom");
        event.setCarId(3);
        event.setCarName("Jag");
        event.setCarSeatConfirmed(0);

        // When
        assertDoesNotThrow(() -> carpoolingUpdateListener.onMessage(event));

        // Then
        verify(carpoolingNotificationsService).notifyCarpoolingUpdate(2, 1, 3, false);
    }
}
