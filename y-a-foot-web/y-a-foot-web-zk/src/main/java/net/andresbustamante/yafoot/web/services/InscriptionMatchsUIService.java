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
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;

@Component
@SessionScope
public class InscriptionMatchsUIService extends AbstractUIService {

    @Value("${api.rest.inscriptions.services.path}")
    private String inscriptionsServicesPath;

    private final Logger log = LoggerFactory.getLogger(InscriptionMatchsUIService.class);

    /**
     * Inscrire le joueur actif dans la session au match passé en paramètre
     *
     * @param match Match auquel on va inscrire le joueur actif
     * @param voiture Voiture dans laquelle le joueur actif va se déplacer
     * @throws ApplicationException
     */
    public void inscrireJoueurMatch(Match match, Voiture voiture) throws ApplicationException {
        Inscription inscription = new Inscription();
        inscription.setIdMatch(match.getId());
        inscription.setJoueur((Joueur) getContexte().getUtilisateur());

        if (voiture != null) {
            inscription.setVoiture(voiture);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(inscriptionsServicesPath);

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Inscription> params = new HttpEntity<>(inscription, headers);

            restTemplate.exchange(builder.toUriString(), HttpMethod.POST, params, String.class);
        } catch (RestClientException e) {
            log.error("Erreur du client REST", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Lancer une demande de desinscription du joueur actif d'un match passé en paramètre
     *
     * @param match Match à quitter
     * @throws ApplicationException
     */
    public void desinscrireJoueurMatch(Match match) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(inscriptionsServicesPath)
                    .path(MessageFormat.format("/{0}", match.getCode()));

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Inscription> params = new HttpEntity<>(headers);

            restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, params, String.class);
        } catch (RestClientException e) {
            log.error("Erreur du client REST", e);
            throw new ApplicationException(e.getMessage());
        }
    }
}
