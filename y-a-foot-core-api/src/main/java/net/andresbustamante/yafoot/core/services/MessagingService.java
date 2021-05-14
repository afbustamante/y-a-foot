package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

import java.util.Locale;

/**
 * Messaging service for email notifications
 */
public interface MessagingService {

    /**
     * Sends a plain text email message with a specific subject to a single destination
     *
     * @param destinationEmail Email address of the destination
     * @param subject Subject of the message
     * @param content Content of the message
     */
    void sendEmail(String destinationEmail, String subject, String content) throws ApplicationException;

    /**
     * Sends a plain text email message with a specific subject to a single destination by using a mail template
     *
     * @param destinationEmail Email address to use for destination
     * @param subjectCode Code of the subject in the messages.properties file
     * @param subjectParameters Parameters used in the subject text
     * @param contentTemplate Name of the template to use
     * @param contentModel Object containing the information needed for the template
     * @param locale
     * @throws ApplicationException
     */
    void sendEmail(String destinationEmail, String subjectCode, String[] subjectParameters,
                   String contentTemplate, Object contentModel, Locale locale) throws ApplicationException;
}
