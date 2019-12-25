package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.model.xs.Sites;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
@SessionScope
public class MatchsRegistryUIService extends AbstractUIService {

    @Value("${api.rest.sites.services.path}")
    private String sitesServicesPath;

    @Value("${api.rest.matches.services.path}")
    private String matchsServicesPath;

    /**
     * Charger la liste de sites disponibles pour l'utilisateur connecté
     *
     * @return Liste de sites trouvés dans l'historique de l'utilisateur connecté
     * @throws ApplicationException
     */
    public List<Site> findSites() throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(sitesServicesPath)
                    .queryParam("pid", getUserContext().getUser().getId());

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Sites> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params, Sites.class);
            return (response.getBody() != null) ? response.getBody().getSite() : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la recherche des sites pour un player", e);
        }
    }

    /**
     * Crée un nouveau match dans le système
     *
     * @param match Le match à créer
     * @return Le code du nouveau match
     * @throws ApplicationException
     */
    public String saveMatch(Match match) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath);

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Match> params = new HttpEntity<>(match, headers);

            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, params,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getHeaders().getLocation() != null) {
                String[] location = response.getHeaders().getLocation().getPath().split("/");
                return location[location.length - 1];
            }
            return null;
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la création d'un match", e);
        }
    }

    /**
     * Lancer une demande d'annulation de match
     *
     * @param match Match à annuler
     * @throws ApplicationException
     */
    public void cancelMatch(Match match) throws ApplicationException {
        throw new UnsupportedOperationException("To be done");
    }
}
