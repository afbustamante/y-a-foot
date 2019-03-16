package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.model.xs.Sites;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class OrganisationMatchsUIService extends AbstractUIService {

    private final transient Logger log = LoggerFactory.getLogger(OrganisationMatchsUIService.class);

    @Value("${rest.services.uri}")
    private String restServerUrl;

    @Value("${recherche.sites.service.path}")
    private String pathSitesService;

    @Value("${gestion.joueurs.service.path}")
    private String pathJoueursService;

    /**
     * Charger la liste de sites disponibles pour l'utilisateur connecté
     *
     * @return Liste de sites trouvés dans l'historique de l'utilisateur connecté
     * @throws ApplicationException
     */
    public List<Site> chercherSites() throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restServerUrl + pathSitesService)
                    .queryParam("idJoueur", getContexte().getUtilisateur().getId());

            ResponseEntity<Sites> response = restTemplate.getForEntity(builder.toUriString(), Sites.class);
            return (response.getBody() != null) ? response.getBody().getSite() : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("Erreur lors de la recherche des sites pour un joueur", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Crée un nouveau match dans le système
     *
     * @param match Le match à créer
     * @return Le code du nouveau match
     * @throws ApplicationException
     */
    public String creerMatch(Match match) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return "";
        } catch (RestClientException e) {
            throw new ApplicationException(e.getMessage());
        }
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
