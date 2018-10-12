package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.model.xs.Sites;
import net.andresbustamante.yafoot.util.ConfigProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
public class OrganisationMatchsUIService extends AbstractUIService {

    private static final String BASE_URI = ConfigProperties.getValue("matchs.services.uri");
    private final Log log = LogFactory.getLog(OrganisationMatchsUIService.class);

    public OrganisationMatchsUIService() {
    }

    public List<Site> chercherSites() throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("recherche.sites.service.path"));
            Sites sites = webTarget.path(MessageFormat.format("/joueur/{0}",
                    getContexte().getUtilisateur().getId())).request(MediaType.APPLICATION_JSON).get(Sites.class);

            if (sites != null) {
                return sites.getSite();
            }
            return Collections.emptyList();
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de la recherche des sites pour un utilisateur", ex);
            throw new ApplicationException("Erreur lors de la recherche des sites : " + ex.getMessage());
        }
    }

    public String creerMatch(Match match) throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("gestion.matchs.service.path"));
            return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(match), String.class);
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de la création du match", ex);
            throw new ApplicationException("Erreur lors de la recherche des sites : " + ex.getMessage());
        }
    }
}
