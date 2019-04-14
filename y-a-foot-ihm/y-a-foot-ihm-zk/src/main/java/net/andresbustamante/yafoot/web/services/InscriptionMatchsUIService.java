package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Voiture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class InscriptionMatchsUIService extends AbstractUIService {

    @Value("${backend.rest.services.uri}")
    private String restServerUrl;

    @Value("${recherche.joueurs.service.path}")
    private String joueursServicesPath;

    @Value("${inscriptions.matchs.service.path}")
    private String inscriptionsServicesPath;

    private final transient Logger log = LoggerFactory.getLogger(InscriptionMatchsUIService.class);

    /**
     * Inscrire le joueur actif dans la session au match passé en paramètre
     *
     * @param match Match auquel on va inscrire le joueur actif
     * @param voiture Voiture dans laquelle le joueur actif va se déplacer
     * @return Succès de l'opération ?
     * @throws ApplicationException
     */
    public boolean inscrireJoueurMatch(Match match, Voiture voiture) throws ApplicationException {
        Inscription inscription = new Inscription();
        inscription.setIdMatch(match.getId());
        inscription.setJoueur((Joueur) getContexte().getUtilisateur());

        if (voiture != null) {
            inscription.setVoiture(voiture);
        }

        try {
            boolean succes = false;

            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restServerUrl + inscriptionsServicesPath);

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Inscription> params = new HttpEntity<>(inscription, headers);

            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, params,
                    String.class);

            succes = (response.getStatusCode().is2xxSuccessful() && response.getHeaders().getLocation() != null);

            return succes;
        } catch (RestClientException e) {
            log.error("Erreur du client REST", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    protected String getServerUrl() {
        return restServerUrl;
    }

    @Override
    protected String getJoueursPath() {
        return joueursServicesPath;
    }
}
