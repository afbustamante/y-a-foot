package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Player;
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

@Component
@SessionScope
public class PlayersRegistryUIService extends AbstractUIService {

    @Value("${api.rest.players.services.path}")
    private String playersServicesPath;

    /**
     * Créer un nouveau joueur dans l'application
     *
     * @param player Joueur à créer
     * @throws ApplicationException
     */
    public boolean savePlayer(Player player) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(playersServicesPath);
            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Player> params = new HttpEntity<>(player, headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, params, Boolean.class);
            boolean succes = (response.getHeaders().getLocation() != null);

            return (response.getStatusCode().is2xxSuccessful() && succes);
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la création d'un nouveau compte", e);
        }
    }
}