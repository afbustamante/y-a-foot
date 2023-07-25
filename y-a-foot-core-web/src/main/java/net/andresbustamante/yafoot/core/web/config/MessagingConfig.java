package net.andresbustamante.yafoot.core.web.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MessagingConfig {

    @Value("${app.messaging.queues.matches.registrations.name}")
    private String playerMatchRegistrationsQueue;

    @Value("${app.messaging.queues.matches.unsubscriptions.name}")
    private String playerMatchUnsubscriptionsQueue;

    @Value("${app.messaging.queues.carpooling.requests.name}")
    private String carpoolingRequestsQueue;

    @Value("${app.messaging.queues.carpooling.updates.name}")
    private String carpoolingUpdatesQueue;

    @Bean
    public Queue playerMatchRegistrationsQueue() {
        return new Queue(playerMatchRegistrationsQueue, false);
    }

    @Bean
    public Queue playersUnsubscriptionsQueue() {
        return new Queue(playerMatchUnsubscriptionsQueue, false);
    }

    @Bean
    public Queue carpoolingRequestsQueue() {
        return new Queue(carpoolingRequestsQueue, false);
    }

    @Bean
    public Queue carpoolingUpdatesQueue() {
        return new Queue(carpoolingUpdatesQueue, false);
    }
}
