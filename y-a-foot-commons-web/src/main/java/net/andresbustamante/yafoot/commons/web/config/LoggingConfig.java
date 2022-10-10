package net.andresbustamante.yafoot.commons.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Value("${api.config.rest.logging.include.headers}")
    private boolean includeHeaders;

    @Value("${api.config.rest.logging.include.query}")
    private boolean includeQueryString;

    @Value("${api.config.rest.logging.include.payload}")
    private boolean includePayload;

    @Value("${api.config.rest.logging.max-payload-length}")
    private Integer maxPayloadLength;

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
