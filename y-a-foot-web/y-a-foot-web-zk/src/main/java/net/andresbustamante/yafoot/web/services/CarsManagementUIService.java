package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Car;
import net.andresbustamante.yafoot.model.xs.Cars;
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

    @Value("${api.rest.cars.services.path}")
    private String carsServicesPath;

    public List<Car> findCarsByUser() throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(carsServicesPath).queryParam("pid", getUserContext().getUser().getId());

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Void> params = new HttpEntity<>(headers);

            ResponseEntity<Cars> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, params,
                    Cars.class);
            return (response.getBody() != null) ? response.getBody().getCar() : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ApplicationException("REST client error when asking for cars from user", e);
        }
    }

    public void addCarForUser(Car car) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(carsServicesPath);

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Car> params = new HttpEntity<>(car, headers);

            ResponseEntity<Long> response = restTemplate.postForEntity(builder.toUriString(), params, Long.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                URI location = response.getHeaders().getLocation();

                if (location != null) {
                    String locationString = location.toString();
                    car.setId(Integer.valueOf(locationString.substring(locationString.lastIndexOf("/"))));
                }
            }
        } catch (RestClientException e) {
            throw new ApplicationException("REST client error when saving a car for user", e);
        }
    }
}
