package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * @author andresbustamante
 */
@Service
public class RechercheMatchsUIService extends AbstractUIService {

    private final transient Logger log = LoggerFactory.getLogger(RechercheMatchsUIService.class);

    @Value("${rest.services.uri}")
    private String restServerUrl;

    @Value("${recherche.joueurs.service.path}")
    private String pathJoueursService;

    @Value("${recherche.match.par.code.path}")
    private String pathRechercheMatchService;

    public Match chercherMatchParCode(String codeMatch) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = restServerUrl + MessageFormat.format(pathRechercheMatchService, codeMatch);

            ResponseEntity<Match> response = restTemplate.getForEntity(url, Match.class);

            return (response.getStatusCode().is2xxSuccessful()) ? response.getBody() : null;
        } catch (RestClientException e) {
            log.error("Erreur lors de la recherche d'un match", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    public Matchs chercherMatchsJoueur(String idJoueur) throws ApplicationException {
        return null;
    }

    @Override
    protected String getServerUrl() {
        return restServerUrl;
    }

    @Override
    protected String getJoueursPath() {
        return pathJoueursService;
    }
}
