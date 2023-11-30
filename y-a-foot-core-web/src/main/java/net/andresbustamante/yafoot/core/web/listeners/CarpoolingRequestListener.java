package net.andresbustamante.yafoot.core.web.listeners;

import net.andresbustamante.yafoot.commons.events.MessagingEventListener;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.core.events.CarpoolingRequestEvent;
import net.andresbustamante.yafoot.core.services.CarpoolingNotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CarpoolingRequestListener implements MessagingEventListener<CarpoolingRequestEvent> {

    private final Logger log = LoggerFactory.getLogger(CarpoolingRequestListener.class);

    private final CarpoolingNotificationsService carpoolingNotificationsService;

    public CarpoolingRequestListener(final CarpoolingNotificationsService carpoolingNotificationsService) {
        this.carpoolingNotificationsService = carpoolingNotificationsService;
    }

    @Override
    @RabbitListener(queues = "${app.messaging.queues.carpooling.requests.name}")
    public void onMessage(final CarpoolingRequestEvent event) {
        log.info("Processing new car request event on match {}", event.getMatchCode());

        try {
            carpoolingNotificationsService.notifyCarpoolingRequest(event.getPlayerId(), event.getMatchId(),
                    event.getCarId());
        } catch (ApplicationException e) {
            throw new RuntimeException(e);
        }
    }
}
