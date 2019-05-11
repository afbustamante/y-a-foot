package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
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

    private Contexte contexte;

    public Contexte getContexte() {
        if (contexte == null) {
            Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants
                    .CONTEXTE);
            if (obj != null) {
                contexte = (Contexte) obj;
            } else {
                Principal user = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

                if (user != null) {
                    String email = user.getName();

                    Joueur joueur = chercherJoueur(email);

                    if (joueur != null) {
                        contexte = new Contexte();
                        contexte.setUtilisateur(joueur);
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                            WebConstants.CONTEXTE, contexte);
                }
            }
        }
        return contexte;
    }

    protected Locale getLocaleUtilisateur() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    private Joueur chercherJoueur(String email) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(BASE_URI).path(ConfigProperties
                .getValue("recherche.joueurs.service.path")).path(MessageFormat.format("{0}/email", email));
        return resource.request(MediaType.APPLICATION_XML).get(Joueur.class);
    }
}
