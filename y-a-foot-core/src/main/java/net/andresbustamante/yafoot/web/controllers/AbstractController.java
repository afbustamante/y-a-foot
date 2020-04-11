package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;

/**
 * Abstract controller for common RESTful controllers operations
 *
 * @author andresbustamante
 */
public abstract class AbstractController {

    private final Logger log = LoggerFactory.getLogger(AbstractController.class);

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
}
