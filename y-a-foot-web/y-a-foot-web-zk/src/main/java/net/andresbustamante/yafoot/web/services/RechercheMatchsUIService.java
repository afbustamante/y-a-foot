package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.text.MessageFormat;

/**
 * @author andresbustamante
 */
@Component
@SessionScope
public class RechercheMatchsUIService extends AbstractUIService {

    private final transient Logger log = LoggerFactory.getLogger(RechercheMatchsUIService.class);

    @Value("${api.rest.joueurs.services.path}")
    private String joueursServicesPath;

    @Value("${api.rest.matchs.services.code.path}")
    private String matchsParCodeServicesPath;

    @Value("${api.rest.matchs.services.path}")
    private String matchsServicesPath;

    public Match chercherMatchParCode(String codeMatch) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath)
                    .path(MessageFormat.format(matchsParCodeServicesPath, codeMatch));

            ResponseEntity<Match> response = restTemplate.getForEntity(builder.toUriString(), Match.class);

            return (response.getStatusCode().is2xxSuccessful()) ? response.getBody() : null;
        } catch (RestClientException e) {
            log.error("Erreur lors de la recherche d'un match", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    public Matchs chercherMatchsJoueur() throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath)
                    .queryParam("idJoueur", getContexte().getUtilisateur().getId());

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Matchs> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params, Matchs.class);

            return (response.getStatusCode().is2xxSuccessful()) ? response.getBody() : null;
        } catch (RestClientException e) {
            log.error("Erreur lors de la recherche d'un match", e);
            throw new ApplicationException(e.getMessage());
        }
    }
}
