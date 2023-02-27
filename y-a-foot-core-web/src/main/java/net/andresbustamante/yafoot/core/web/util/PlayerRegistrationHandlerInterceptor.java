package net.andresbustamante.yafoot.core.web.util;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.PlayerManagementService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Set;

/**
 * Intercepts every http call to check if the player is already registered. Otherwise, it will created the player
 * using the authenticated principal data.
 */
public class PlayerRegistrationHandlerInterceptor implements HandlerInterceptor {

    private static final Collection<HttpMethod> METHODS_TO_INTERCEPT = Set.of(
            HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH);

    private final PlayerManagementService playerManagementService;
    private final PlayerSearchService playerSearchService;

    private final Logger log = LoggerFactory.getLogger(PlayerRegistrationHandlerInterceptor.class);

    /**
     * Public constructor.
     *
     * @param playerManagementService
     * @param playerSearchService
     */
    public PlayerRegistrationHandlerInterceptor(
            PlayerManagementService playerManagementService, PlayerSearchService playerSearchService) {
        this.playerManagementService = playerManagementService;
        this.playerSearchService = playerSearchService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (METHODS_TO_INTERCEPT.contains(HttpMethod.resolve(request.getMethod()))) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader != null) {
                // Check if the authenticated user is already registered as a player
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication instanceof JwtAuthenticationToken) {
                    Jwt credentials = (Jwt) authentication.getCredentials();
                    String username = credentials.getClaimAsString("email");

                    Player player = playerSearchService.findPlayerByEmail(username);

                    if (player == null) {
                        log.info("User does not exist as a player: {}", username);
                        createPlayerFromCredentials(credentials);
                    } else {
                        log.debug("User {} is already registered as a player.", username);
                    }
                } else {
                    log.warn("No user to check");
                }
            } else {
                log.warn("No auth header found for request on {}", request.getPathInfo());
            }
        }

        return true;
    }

    private void createPlayerFromCredentials(Jwt credentials) throws DatabaseException, ApplicationException {
        String username = credentials.getClaimAsString("email");

        Player newPlayer = new Player();
        newPlayer.setFirstName(credentials.getClaimAsString("given_name"));
        newPlayer.setSurname(credentials.getClaimAsString("family_name"));
        newPlayer.setEmail(username);
        newPlayer.setActive(true);

        UserContext userContext = new UserContext();
        userContext.setUsername(username);

        int id = playerManagementService.savePlayer(newPlayer, userContext);
        log.info("Player {} ({}) successfully registered from token information", id, username);
    }
}
