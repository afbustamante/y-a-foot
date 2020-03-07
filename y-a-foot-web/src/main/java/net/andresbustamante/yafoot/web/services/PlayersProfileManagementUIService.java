package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Player;
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

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Component
@SessionScope
public class PlayersProfileManagementUIService extends AbstractUIService {

    @Value("${api.rest.players.services.path}")
    private String playersServicesPath;

    @Value("${api.rest.players.byemail.services.path}")
    private String playersByEmailServicesPath;

    public Player loadProfileForPlayer() throws ApplicationException {
        return (Player) getUserContext().getUser();
    }

    public boolean updateProfile(Player player) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(playersServicesPath)
                    .path(MessageFormat.format(playersByEmailServicesPath, player.getEmail()));

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Player> params = new HttpEntity<>(player, headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.PUT, params, Boolean.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la mise à jour des données", e);
        }
    }

    public boolean deactivatePlayer(Player player) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(playersServicesPath)
                    .path(MessageFormat.format(playersByEmailServicesPath, player.getEmail()));

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Player> params = new HttpEntity<>(headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.DELETE, params, Boolean.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            throw new ApplicationException("Erreur lors de la mise à jour des données", e);
        }
    }

    public boolean updatePlayerPassword(String emailJoueur, String motDePasse) throws ApplicationException {

        Player player = new Player();
        player.setEmail(emailJoueur);
        player.setPassword(motDePasse.getBytes(StandardCharsets.UTF_8));

        return updateProfile(player);
    }
}
