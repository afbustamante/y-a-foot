package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.util.ConfigProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * @author andresbustamante
 */
public class OrganisationMatchsUIService extends AbstractUIService {

    private static final String BASE_URI = ConfigProperties.getValue("matchs.services.uri");
    private final Log log = LogFactory.getLog(OrganisationMatchsUIService.class);

    public OrganisationMatchsUIService() {
    }

    public String creerMatch(Match match) throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("gestion.matchs.service.path"));
            return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(match), String.class);
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de la création du match", ex);
            return "";
        }
    }
}
