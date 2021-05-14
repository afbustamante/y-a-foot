package net.andresbustamante.yafoot.services.impl;

import freemarker.template.Configuration;
import net.andresbustamante.yafoot.commons.services.AbstractServiceIntegrationTest;
import net.andresbustamante.yafoot.config.FreemarkerTestConfig;
import net.andresbustamante.yafoot.config.MessagingTestConfig;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link MessagingServiceImpl}
 */
@ContextConfiguration(classes = {MessagingTestConfig.class, FreemarkerTestConfig.class})
class MessagingServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DelegatingMessageSource delegatingMessageSource;

    private MessagingService messagingService;

    @BeforeEach
    public void setUp() {
        messagingService = new MessagingServiceImpl(mailSender, freemarkerConfiguration, applicationContext);
    }

    @Test
    void sendEmail() {
        assertDoesNotThrow(() -> messagingService.sendEmail("root@localhost", "subject", "content"));
    }

    @Test
    void sendEmailToInvalidDestination() {
        assertThrows(ApplicationException.class, () -> messagingService.sendEmail("not-an-email", "subject", "content"));
    }

    @Test
    void testSendEmailUnknownTemplate() throws Exception {
        String name = "Test Name";
        User model = new User("test@email.com");

        assertThrows(ApplicationException.class, () -> messagingService.sendEmail("root@localhost", "test.subject",
                new String[]{name}, "unknown-template.ftl", model, Locale.ENGLISH));
    }

    @Test
    void testSendEmailValidTemplate() throws Exception {
        prepareMessageSource();

        // Given
        String name = "Test Name";
        User model = new User("test@email.com");

        // When
        // Then
        assertDoesNotThrow(() -> messagingService.sendEmail("root@localhost", "test.subject",
                new String[]{name}, "test-template.ftl", model, Locale.ENGLISH));
    }

    private void prepareMessageSource() {
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        delegatingMessageSource.setParentMessageSource(messageSource);
    }
}