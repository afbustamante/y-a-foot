package net.andresbustamante.yafoot.messaging.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * Messaging service for email notifications.
 */
public interface MessagingService {

    /**
     * Sends a plain text email message with a specific subject to a single destination.
     *
     * @param destinationEmail Email address of the destination
     * @param subject Subject of the message
     * @param content Content of the message
     */
    void sendEmail(String destinationEmail, String subject, String content) throws ApplicationException;
}
