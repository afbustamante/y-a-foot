package net.andresbustamante.yafoot.commons.web.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.util.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;

/**
 * Abstract controller for common RESTful controllers operations.
 *
 * @author andresbustamante
 */
@Validated
public abstract class AbstractController {

    /* Common error messages */
    /**
     * Code to use for generic database errors.
     */
    protected static final String DATABASE_BASIC_ERROR = "database.basic.error";

    /**
     * Code to use for authorisation errors.
     */
    protected static final String UNAUTHORISED_USER_ERROR = "unauthorised.user.error";

    /**
     * Injected request (constructors only).
     */
    private final HttpServletRequest request;

    /**
     * Injected application context (constructors only).
     */
    private final ApplicationContext applicationContext;

    /**
     * Default object mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * Public URL to access a controller from this app.
     */
    @Value("${api.config.public.url}")
    private String apiPublicUrl;

    /**
     * Default constructor.
     *
     * @param request HTTP servlet request
     * @param objectMapper Object mapper to use
     * @param applicationContext Application context
     */
    protected AbstractController(HttpServletRequest request, ObjectMapper objectMapper,
                                 ApplicationContext applicationContext) {
        this.request = request;
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Transforms a ConstraintViolationException into a 400 error code.
     *
     * @param e Exception to check
     * @return ResponseEntity with a "Bad request" code and message
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Void> handleConstraintViolationException(ConstraintViolationException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Builds a basic URI for a location header.
     *
     * @param location Relative location
     * @return URI with absolute location
     */
    protected URI getLocationURI(String location) {
        return URI.create(apiPublicUrl + location);
    }

    /**
     * Gets the connected user from a given request.
     *
     * @return Connected user's context
     */
    protected UserContext getUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (authentication instanceof AnonymousAuthenticationToken) ? "anonymous"
                : authentication.getName();
        String timeZone = request.getHeader(UserContext.TZ);

        UserContext userContext = new UserContext(username);
        userContext.setTimezone(timeZone != null ? ZoneId.of(timeZone) : ZoneId.systemDefault());
        return userContext;
    }

    /**
     * Translates a given code using some parameters into the user's language.
     *
     * @param messageCode Code to find for translation
     * @param parameters Parameters to replace on the translated message
     * @return Translated message
     */
    protected String translate(String messageCode, String[] parameters) {
        return applicationContext.getMessage(messageCode, parameters, getUserLocale());
    }

    /**
     * Finds the user's locale using the HTTP request.
     *
     * @return Main locale for the connected user
     */
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

    /**
     * OpenAPI auto-generated method.
     *
     * @return Optional for object mapper
     */
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.of(objectMapper);
    }

    /**
     * OpenAPI auto-generated method.
     *
     * @return Optional for object mapper
     */
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }
}
