package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Voiture;
import net.andresbustamante.yafoot.model.xs.Voitures;
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

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Component
@SessionScope
public class CarsManagementUIService extends AbstractUIService {

    @Value("${api.rest.voitures.services.path}")
    private String carsServicesPath;

    public List<Voiture> findCarsByUser() throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(carsServicesPath).queryParam("pid", getContexte().getUtilisateur().getId());

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Voitures> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params,
                    Voitures.class);
            return (response.getBody() != null) ? response.getBody().getVoiture() : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ApplicationException("REST client error when asking for cars from user", e);
        }
    }

    public void addCarForUser(Voiture voiture) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(carsServicesPath);

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Voiture> params = new HttpEntity<>(voiture, headers);

            ResponseEntity<Long> response = restTemplate.postForEntity(builder.toUriString(), params, Long.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                URI location = response.getHeaders().getLocation();

                if (location != null) {
                    String locationString = location.toString();
                    voiture.setId(Integer.valueOf(locationString.substring(locationString.lastIndexOf("/"))));
                }
            }
        } catch (RestClientException e) {
            throw new ApplicationException("REST client error when saving a car for user", e);
        }
    }
}
