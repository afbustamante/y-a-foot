package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.util.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

/**
 * Abstract controller for common RESTful controllers operations
 *
 * @author andresbustamante
 */
public abstract class AbstractController {

    /* Common context headers */
    protected static final String CTX_MESSAGES = "ctx-messages";

    /* Common error messages */
    protected static final String DATABASE_BASIC_ERROR = "database.basic.error";
    protected static final String INVALID_USER_ERROR = "invalid.user.error";

    /**
     * Injected request (constructors only)
     */
    protected HttpServletRequest request;

    @Autowired
    private ApplicationContext applicationContext;

    private final Logger log = LoggerFactory.getLogger(AbstractController.class);

    abstract Optional<HttpServletRequest> getRequest();

    protected URI getLocationURI(String location) {
        try {
            return new URI(location);
        } catch (URISyntaxException e) {
            log.error("An error occurred while building a location URI for a new resource", e);
            return null;
        }
    }

    protected UserContext getUserContext(HttpServletRequest request) throws ApplicationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // User successfully authenticated
            String username = authentication.getName();
            String timeZone = request.getHeader(UserContext.TZ);

            UserContext userContext = new UserContext(username);
            userContext.setTimezone(timeZone != null ? ZoneId.of(timeZone) : ZoneId.systemDefault());
            return userContext;
        } else {
            // Anonymous user
            throw new ApplicationException("Unable to find a username");
        }
    }

    protected MultiValueMap<String, String> buildMessageHeader(String messageCode, String[] messageParameters) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(CTX_MESSAGES, Collections.singletonList(translate(messageCode, messageParameters)));
        return headers;
    }

    private String translate(String messageCode, String[] parameters) {
        return applicationContext.getMessage(messageCode, parameters, getUserLocale());
    }

    private Locale getUserLocale() {
        if (request != null) {
            String acceptedLanguages = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

            if (acceptedLanguages != null) {
                // Example: es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3
                String[] headerParts = acceptedLanguages.split("(,|;)");

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
        }
        return LocaleUtils.DEFAULT_LOCALE;
    }
}
