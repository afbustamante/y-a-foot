package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.model.xs.UserContext;
import net.andresbustamante.yafoot.model.xs.Player;
import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.WebConstants;

import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    protected static final String BASE_URI = ConfigProperties.getValue("rest.services.uri");

    private UserContext userContext;

    public UserContext getUserContext() {
        if (userContext == null) {
            Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants
                    .CONTEXTE);
            if (obj != null) {
                userContext = (UserContext) obj;
            } else {
                Principal user = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

                if (user != null) {
                    String email = user.getName();

                    Player player = chercherJoueur(email);

                    if (player != null) {
                        userContext = new UserContext();
                        userContext.setUser(player);
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                            WebConstants.CONTEXTE, userContext);
                }
            }
        }
        return userContext;
    }

    protected Locale getLocaleUtilisateur() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    private Player chercherJoueur(String email) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(BASE_URI).path(ConfigProperties
                .getValue("players.api.service.path")).path(MessageFormat.format("{0}/email", email));
        return resource.request(MediaType.APPLICATION_XML).get(Player.class);
    }
}
