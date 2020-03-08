package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author andresbustamante
 */
public abstract class AbstractController {

    private final Logger log = LoggerFactory.getLogger(AbstractController.class);

    protected URI getLocationURI(String location) {
        try {
            return new URI(location);
        } catch (URISyntaxException e) {
            log.error("Erreur lors de la construction de la location d'une ressource", e);
            return null;
        }
    }

    protected UserContext getUserContext(HttpServletRequest request) throws ApplicationException {
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        String timeZone = request.getHeader(UserContext.TZ);

        if (username != null) {
            UserContext userContext = new UserContext(username);
            userContext.setTimezone(timeZone != null ? ZoneId.of(timeZone) : ZoneId.systemDefault());
            return userContext;
        } else {
            throw new ApplicationException("Unable to find a username");
        }
    }
}
