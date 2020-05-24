package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;

/**
 * Messaging service for email notifications
 */
public interface MessagingService {

    /**
     * Sends a plain text email message with a specific subject to a single destination
     *
     * @param subject Subject of the message
     * @param content Content of the message
     * @param destinationName Name of the destination
     * @param destinationEmail Email address of the destination
     * @throws ApplicationException
     */
    void sendEmail(String subject, String content, String destinationName, String destinationEmail) throws ApplicationException;
}
