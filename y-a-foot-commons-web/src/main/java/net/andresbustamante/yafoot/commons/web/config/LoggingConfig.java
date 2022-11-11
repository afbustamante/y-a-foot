package net.andresbustamante.yafoot.commons.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Logging configuration for incoming requests on web applications.
 */
@Configuration
public class LoggingConfig {

    /**
     * Whether to include request headers.
     */
    @Value("${api.config.rest.logging.include.headers}")
    private boolean includeHeaders;

    /**
     * Whether to include request query parameters.
     */
    @Value("${api.config.rest.logging.include.query}")
    private boolean includeQueryString;

    /**
     * Whether to include request payload.
     */
    @Value("${api.config.rest.logging.include.payload}")
    private boolean includePayload;

    /**
     * Maximal payload length.
     */
    @Value("${api.config.rest.logging.max-payload-length}")
    private Integer maxPayloadLength;

    /**
     * Builds a logging filter using the given parameters.
     *
     * @return Logging filter to use with the given parameters
     */
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(includeQueryString);
        filter.setIncludePayload(includePayload);
        filter.setMaxPayloadLength(maxPayloadLength);
        filter.setIncludeHeaders(includeHeaders);
        return filter;
    }
}
