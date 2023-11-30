package net.andresbustamante.yafoot.core.web.listeners;

import net.andresbustamante.yafoot.commons.events.MessagingEventListener;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.core.events.CarpoolingUpdateEvent;
import net.andresbustamante.yafoot.core.services.CarpoolingNotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CarpoolingUpdateListener implements MessagingEventListener<CarpoolingUpdateEvent> {

    private final Logger log = LoggerFactory.getLogger(CarpoolingUpdateListener.class);

    private final CarpoolingNotificationsService carpoolingNotificationsService;

    public CarpoolingUpdateListener(final CarpoolingNotificationsService carpoolingNotificationsService) {
        this.carpoolingNotificationsService = carpoolingNotificationsService;
    }

    @Override
    @RabbitListener(queues = "${app.messaging.queues.carpooling.updates.name}")
    public void onMessage(final CarpoolingUpdateEvent event) {
        log.info("Processing new carpooling update event on match {}", event.getMatchCode());

        boolean isCarSeatConfirmed = event.getCarSeatConfirmed() == 1;

        try {
            carpoolingNotificationsService.notifyCarpoolingUpdate(event.getPlayerId(), event.getMatchId(),
                    event.getCarId(), isCarSeatConfirmed);
        } catch (ApplicationException e) {
            log.error("An error occurred while notifying a carpooling update", e);
        }
    }
}
