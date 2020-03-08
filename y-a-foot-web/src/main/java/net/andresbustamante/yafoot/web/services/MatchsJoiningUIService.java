package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Registration;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;

@Component
@SessionScope
public class MatchsJoiningUIService extends AbstractUIService {

    @Value("${api.rest.matches.services.path}")
    private String matchsServicesPath;

    @Value("${api.rest.matches.bycode.services.path}")
    private String matchsServicesByCodePath;

    /**
     * Inscrire le joueur actif dans la session au match passé en paramètre
     *
     * @param match Match auquel on va inscrire le joueur actif
     * @param car Voiture dans laquelle le joueur actif va se déplacer
     * @throws ApplicationException
     */
    public void registerPlayer(Match match, Car car) throws ApplicationException {
        Registration registration = new Registration();
        registration.setMatchId(match.getId());
        registration.setPlayer(getUserContext().getPlayer());

        if (car != null) {
            registration.setCar(car);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath).path(MessageFormat.format(matchsServicesByCodePath, match.getCode())).path("/players");

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Registration> params = new HttpEntity<>(registration, headers);

            restTemplate.exchange(builder.toUriString(), HttpMethod.POST, params, String.class);
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur du client REST lors de l'inscription d'un player", e);
        }
    }

    /**
     * Lancer une demande de desinscription du joueur actif d'un match passé en paramètre
     *
     * @param match Match à quitter
     * @throws ApplicationException
     */
    public void unregisterPlayerFromMatch(Match match) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(matchsServicesPath)
                    .path(MessageFormat.format(matchsServicesByCodePath, match.getCode()))
                    .path(MessageFormat.format("/players/{0}", "0")); // TODO Implement player ID recovery

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Registration> params = new HttpEntity<>(headers);

            restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, params, String.class);
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur du client REST lors de la desincription d'un joueur", e);
        }
    }
}
