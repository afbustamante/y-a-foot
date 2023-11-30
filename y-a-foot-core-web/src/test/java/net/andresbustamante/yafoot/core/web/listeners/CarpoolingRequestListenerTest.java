package net.andresbustamante.yafoot.core.web.listeners;

import net.andresbustamante.yafoot.core.events.CarpoolingRequestEvent;
import net.andresbustamante.yafoot.core.services.CarpoolingNotificationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarpoolingRequestListenerTest {

    @InjectMocks
    private CarpoolingRequestListener carpoolingRequestListener;

    @Mock
    private CarpoolingNotificationsService carpoolingNotificationsService;

    @Test
    void testOnMessage() throws Exception {
        // Given
        CarpoolingRequestEvent event = new CarpoolingRequestEvent();
        event.setMatchId(1);
        event.setMatchCode("ABC");
        event.setPlayerId(2);
        event.setPlayerFirstName("Tom");
        event.setCarId(3);
        event.setCarName("Jag");

        // When
        assertDoesNotThrow(() -> carpoolingRequestListener.onMessage(event));

        // Then
        verify(carpoolingNotificationsService).notifyCarpoolingRequest(2, 1, 3);
    }
}
