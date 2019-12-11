package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Player;
import net.andresbustamante.yafoot.model.xs.UserContext;
import net.andresbustamante.yafoot.web.util.WebConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import java.text.MessageFormat;
import java.util.Collections;

import static net.andresbustamante.yafoot.model.UserContext.TZ;
import static net.andresbustamante.yafoot.model.UserContext.USER_CTX;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Service abstrait à étendre par tous les services utilisés depuis la couche UI
 *
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    @Value("${api.rest.services.url}")
    protected String backendServicesUrl;

    @Value("${api.rest.players.services.path}")
    private String playersServicesPath;

    @Value("${api.rest.players.byemail.services.path}")
    private String playersByEmailServicesPath;

    private UserContext userContext;

    /**
     * Récupérer les informations du contexte de l'utilisateur
     *
     * @return
     * @throws ApplicationException Si un problème parvient lors de la récupération des infos du player depuis le serveur
     */
    protected UserContext getUserContext() throws ApplicationException {
        if (userContext == null) {
            Session session = Sessions.getCurrent();
            Object obj = session.getAttribute(WebConstants.CONTEXTE);

            if (obj != null) {
                userContext = (UserContext) obj;
            } else {
                SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
                String email = userDetails.getUsername();

                Player player = findPlayerByEmail(email);

                if (player != null) {
                    userContext = new UserContext();
                    userContext.setUser(player);
                    session.setAttribute(WebConstants.CONTEXTE, userContext);
                }
            }
        }
        return userContext;
    }

    /**
     *
     * @return
     * @throws ApplicationException
     */
    protected MultiValueMap<String, String> getHeadersMap() throws ApplicationException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(USER_CTX, Collections.singletonList(getUserContext().getUser().getId().toString()));
        headers.put(TZ, Collections.singletonList("CET")); // TODO Injecter la timezone à partir de la session
        return headers;
    }

    /**
     * Chercher les informations du joueur connecté à partir de son adresse mail
     *
     * @param email Adresse mail du joueur
     * @return Informations du joueur
     * @throws ApplicationException Si un problème parvient lors des échanges avec le serveur
     */
    private Player findPlayerByEmail(String email) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(playersServicesPath)
                    .path(MessageFormat.format(playersByEmailServicesPath, email));
            ResponseEntity<Player> response = restTemplate.getForEntity(builder.toUriString(), Player.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la récupération des informations d'un joueur", e);
        }
    }
}
