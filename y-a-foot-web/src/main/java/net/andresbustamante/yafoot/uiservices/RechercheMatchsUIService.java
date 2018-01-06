package net.andresbustamante.yafoot.uiservices;

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

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ConfigProperties.getValue("matchs.services.uri");

    public RechercheMatchsUIService() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("recherche.matchs.service.path"));
    }

    public <T> T getMatchParCode(Class<T> responseType, String codeMatch) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("code/{0}", codeMatch));
        return resource.request(MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T getMatchsJoueur(Class<T> responseType, String idJoueur) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("joueur/{0}", idJoueur));
        return resource.request(MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }
}
