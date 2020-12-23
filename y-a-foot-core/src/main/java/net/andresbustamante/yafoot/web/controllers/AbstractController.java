package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.util.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;

/**
 * Abstract controller for common RESTful controllers operations
 *
 * @author andresbustamante
 */
@CrossOrigin
public abstract class AbstractController {

    /* Common error messages */
    protected static final String DATABASE_BASIC_ERROR = "database.basic.error";
    protected static final String UNAUTHORISED_USER_ERROR = "unauthorised.user.error";

    /**
     * Injected request (constructors only)
     */
    protected HttpServletRequest request;

    /**
     * Injected application context (constructors only)
     */
    protected ApplicationContext applicationContext;

    protected ObjectMapper objectMapper;

    @Value("${api.public.url}")
    protected String apiPublicUrl;

    protected AbstractController(HttpServletRequest request, ObjectMapper objectMapper, ApplicationContext applicationContext) {
        this.request = request;
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * OpenAPI auto-generated method
     */
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.of(objectMapper);
    }

    /**
     * OpenAPI auto-generated method
     */
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }

    protected URI getLocationURI(String location) {
        return URI.create(apiPublicUrl + location);
    }

    protected UserContext getUserContext(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (authentication instanceof AnonymousAuthenticationToken) ? "anonymous" : authentication.getName();
        String timeZone = request.getHeader(UserContext.TZ);

        UserContext userContext = new UserContext(username);
        userContext.setTimezone(timeZone != null ? ZoneId.of(timeZone) : ZoneId.systemDefault());
        return userContext;
    }

    protected String translate(String messageCode, String[] parameters) {
        return applicationContext.getMessage(messageCode, parameters, getUserLocale());
    }

    private Locale getUserLocale() {
        String acceptedLanguages = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

        if (acceptedLanguages != null) {
            // Example: es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3
            String[] headerParts = acceptedLanguages.split("([,;])");

            for (String part : headerParts) {
                // Return the first supported locale found in the list
                if (part.matches("[a-z]{2}-[a-zA-Z]{2}")) {
                    Locale locale = Locale.forLanguageTag(part);

                    if (LocaleUtils.isSupportedLocale(locale)) {
                        return locale;
                    }
                } else if (part.matches("[a-z]{2}")) {
                    Locale locale = new Locale(part);

                    if (LocaleUtils.isSupportedLocale(locale)) {
                        return locale;
                    }
                }
            }
        }
        return LocaleUtils.DEFAULT_LOCALE;
    }
}
