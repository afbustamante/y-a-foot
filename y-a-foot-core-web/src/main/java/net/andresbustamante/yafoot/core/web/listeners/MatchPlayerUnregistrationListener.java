package net.andresbustamante.yafoot.core.web.listeners;

import net.andresbustamante.yafoot.commons.events.MessagingEventListener;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.core.events.MatchPlayerUnsubscriptionEvent;
import net.andresbustamante.yafoot.core.services.MatchAlertingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MatchPlayerUnregistrationListener implements MessagingEventListener<MatchPlayerUnsubscriptionEvent> {

    private final Logger log = LoggerFactory.getLogger(MatchPlayerUnregistrationListener.class);

    private final MatchAlertingService matchAlertingService;

    public MatchPlayerUnregistrationListener(MatchAlertingService matchAlertingService) {
        this.matchAlertingService = matchAlertingService;
    }

    @Override
    @RabbitListener(queues = "${app.messaging.queues.matches.unsubscriptions.name}")
    public void onMessage(MatchPlayerUnsubscriptionEvent event) {
        log.info("Processing new event from a player {} removed from match {}", event.getPlayerFirstName(),
                event.getMatchCode());

        try {
            matchAlertingService.checkForAlertsAfterPlayerRemovedFromMatch(event.getMatchId(), event.getPlayerId());
        } catch (ApplicationException e) {
            throw new RuntimeException(e);
        }
    }
}
