package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.util.ConfigProperties;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * @author andresbustamante
 */
public class RechercheMatchsUIService {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ConfigProperties.getValue("matchs.services.uri");

    public RechercheMatchsUIService() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("recherche.matchs.service.path"));
    }

    public Match getMatchParCode(String codeMatch) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("/code/{0}", codeMatch));
        return resource.request(MediaType.APPLICATION_JSON).get(Match.class);
    }

    public Match[] getMatchsJoueur(String idJoueur) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("/joueur/{0}", idJoueur));
        return resource.request(MediaType.APPLICATION_JSON).get(Match[].class);
    }

    public void close() {
        client.close();
    }
}
