package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.ConstantesWeb;

import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.text.MessageFormat;

/**
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    protected static final String BASE_URI = ConfigProperties.getValue("matchs.services.uri");

    private Contexte contexte;

    public Contexte getContexte() {
        if (contexte == null) {
            Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(ConstantesWeb
                    .CONTEXTE);
            if (obj != null) {
                contexte = (Contexte) obj;
            } else {
                Principal user = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

                if (user != null) {
                    String email = user.getName();

                    Joueur joueur = chercherJoueur(email);
                    contexte = new Contexte();

                    if (joueur != null) {
                        contexte.setEmailUtilisateur(joueur.getEmail());
                        contexte.setIdUtilisateur(joueur.getId());
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                            ConstantesWeb.CONTEXTE, contexte);
                }
            }
        }
        return contexte;
    }

    private Joueur chercherJoueur(String email) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(ConfigProperties.getValue("matchs.services.uri")).path(ConfigProperties
                .getValue("recherche.joueurs.service.path")).path(MessageFormat.format("{0}/email", email));
        return resource.request(MediaType.APPLICATION_JSON).get(Joueur.class);
    }
}
