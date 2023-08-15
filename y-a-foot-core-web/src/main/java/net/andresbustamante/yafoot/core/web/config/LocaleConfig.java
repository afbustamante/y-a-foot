package net.andresbustamante.yafoot.core.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Locale configuration.
 */
@Configuration
public class LocaleConfig {

    @Value("${app.timezone}")
    private String timezone;

    /**
     * Sets the default timezone.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
    }
}
