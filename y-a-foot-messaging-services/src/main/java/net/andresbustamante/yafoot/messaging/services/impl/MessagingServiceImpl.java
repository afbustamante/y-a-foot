package net.andresbustamante.yafoot.messaging.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.sender.address}")
    private String mailSenderAddress;

    @Value("${app.mail.sender.name}")
    private String mailSenderName;

    private final Logger log = LoggerFactory.getLogger(MessagingServiceImpl.class);

    @Autowired
    public MessagingServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(String destinationEmail, String subject, String content) throws ApplicationException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            Address toAddress = new InternetAddress(destinationEmail);
            Address fromAddress = new InternetAddress(mailSenderAddress, mailSenderName);

            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());
            message.setContent(content, "text/plain; charset=UTF-8");

            Transport.send(message);
            log.info("Message sent to '{}' with the subject '{}'", destinationEmail, subject);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new ApplicationException("An error occurred while sending an email", e);
        }
    }
}
