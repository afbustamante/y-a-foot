package net.andresbustamante.yafoot.services.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.services.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Locale;

@Service
public class MessagingServiceImpl implements MessagingService {

    private JavaMailSender mailSender;

    private Configuration freemarkerConfiguration;

    private ApplicationContext applicationContext;

    @Value("${app.mail.sender.address}")
    private String mailSenderAddress;

    @Value("${app.mail.sender.name}")
    private String mailSenderName;

    private final Logger log = LoggerFactory.getLogger(MessagingServiceImpl.class);

    @Autowired
    public MessagingServiceImpl(JavaMailSender mailSender, Configuration freemarkerConfiguration,
                                ApplicationContext applicationContext) {
        this.mailSender = mailSender;
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.applicationContext = applicationContext;
    }

    @Override
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
            log.info("Message sent");
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new ApplicationException("An error occurred while sending an email", e);
        }
    }

    @Override
    public void sendEmail(String destinationEmail, String subjectCode, String[] subjectParameters,
                          String contentTemplate, Object contentModel, Locale locale) throws ApplicationException {
        try {
            Template t = freemarkerConfiguration.getTemplate(contentTemplate);
            String messageContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, contentModel);
            String subject = applicationContext.getMessage(subjectCode, subjectParameters, locale);

            sendEmail(destinationEmail, subject, messageContent);
        } catch (TemplateException e) {
            throw new ApplicationException("Invalid template", e);
        } catch (TemplateNotFoundException e) {
            throw new ApplicationException("Unknown template", e);
        } catch (IOException e) {
            throw new ApplicationException("Unable to load template", e);
        }
    }
}
