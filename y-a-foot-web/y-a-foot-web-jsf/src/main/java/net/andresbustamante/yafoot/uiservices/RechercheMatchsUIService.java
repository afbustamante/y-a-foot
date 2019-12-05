package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matches;
import net.andresbustamante.yafoot.util.ConfigProperties;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;

/**
 * @author andresbustamante
 */
public class RechercheMatchsUIService extends AbstractUIService {

    public Match chercherMatchParCode(String codeMatch) throws ClientErrorException {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(BASE_URI).path(ConfigProperties.getValue("matches.api.service.path"));
        resource = resource.path(MessageFormat.format("/{0}", codeMatch));
        return resource.request(MediaType.APPLICATION_XML).get(Match.class);
    }

    public Matches chercherMatchsJoueur(String idJoueur) throws ClientErrorException {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(BASE_URI).path(ConfigProperties.getValue("matches.api.service.path"));
        resource = resource.queryParam("playerId", idJoueur);
        return resource.request(MediaType.APPLICATION_XML).get(Matches.class);
    }
}
