package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.web.dto.UserContext;
import net.andresbustamante.yafoot.web.util.WebConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import java.util.Base64;
import java.util.Collections;

import static net.andresbustamante.yafoot.model.UserContext.TZ;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Service abstrait à étendre par tous les services utilisés depuis la couche UI
 *
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    private static final String BASIC_AUTH = "Basic ";

    @Value("${api.rest.services.url}")
    protected String backendServicesUrl;

    @Value("${api.rest.services.username}")
    private String backendServicesUsername;

    @Value("${api.rest.services.password}")
    private String backendServicesPassword;

    @Value("${api.rest.players.services.path}")
    private String playersServicesPath;

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
                    userContext.setPlayer(player);
                    session.setAttribute(WebConstants.CONTEXTE, userContext);
                }
            }
        }
        return userContext;
    }

    /**
     * Build headers map for common user requests
     *
     * @return
     * @throws ApplicationException
     */
    protected MultiValueMap<String, String> getHeadersMap() throws ApplicationException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList(BASIC_AUTH + getBase64EncodedPassword()));
        headers.put(TZ, Collections.singletonList("CET")); // TODO Injecter la timezone à partir de la session
        return headers;
    }

    /**
     * Build headers map for anonymous user requests (e.g. sign in)
     *
     * @return
     * @throws ApplicationException
     */
    protected MultiValueMap<String, String> getAnonymousHeadersMap() throws ApplicationException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList(BASIC_AUTH + getBase64EncodedPassword()));
        return headers;
    }

    private String getBase64EncodedPassword() {
        String source = backendServicesUsername + ":" + backendServicesPassword;
        return Base64.getEncoder().encodeToString(source.getBytes());
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
                    .queryParam("email", email);
            MultiValueMap<String, String> headers = getAnonymousHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Player> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params,
                    Player.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la récupération des informations d'un joueur", e);
        }
    }
}
