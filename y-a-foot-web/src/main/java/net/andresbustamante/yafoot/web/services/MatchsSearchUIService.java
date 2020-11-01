package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.dto.Registration;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
@Component
@SessionScope
public class MatchsSearchUIService extends AbstractUIService {

    @Value("${api.rest.matches.bycode.services.path}")
    private String matchsParCodeServicesPath;

    @Value("${api.rest.matches.services.path}")
    private String matchsServicesPath;

    public Match findMatchByCode(String codeMatch) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath)
                    .path(MessageFormat.format(matchsParCodeServicesPath, codeMatch));
            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Match> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params, Match.class);

            return (response.getStatusCode().is2xxSuccessful()) ? response.getBody() : null;
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la recherche d'un match", e);
        }
    }

    public List<Match> findMatchesForPlayer() throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath)
                    .queryParam("pid", getUserContext().getPlayer().getId());

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Match[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params, Match[].class);

            return (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) ?
                    Arrays.asList(response.getBody()) : null;
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la recherche des matchs pour un joueur", e);
        }
    }

    public List<Registration> findMatchRegistrations(Match match) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath)
                    .path(MessageFormat.format(matchsParCodeServicesPath, match.getCode()))
                    .path("/registrations");
            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Registration[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params, Registration[].class);

            return (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la recherche d'un match", e);
        }
    }
}
