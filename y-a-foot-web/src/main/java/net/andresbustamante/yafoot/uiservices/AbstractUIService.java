package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.ConstantesWeb;
import net.andresbustamante.yafoot.xs.Contexte;
import net.andresbustamante.yafoot.xs.Joueur;
import net.andresbustamante.yafoot.xs.UtilisateurType;

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

                    UtilisateurType utilisateur = chargerUtilisateur(email);

                    if (utilisateur != null) {
                        contexte = new Contexte();
                        contexte.setUtilisateur(utilisateur);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                                ConstantesWeb.CONTEXTE, contexte);
                    }
                }
            }
        }
        return contexte;
    }

    private UtilisateurType chargerUtilisateur(String email) {
        UtilisateurType utilisateur = null;

        Joueur joueur = chercherJoueur(email);

        if (joueur != null) {
            utilisateur = new UtilisateurType();
            utilisateur.setEmail(email);
            utilisateur.setId(joueur.getId());
        }

        return utilisateur;
    }

    private Joueur chercherJoueur(String email) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(ConfigProperties.getValue("matchs.services.uri")).path(ConfigProperties
                .getValue("recherche.joueurs.service.path")).path(MessageFormat.format("joueur/{0}", email));
        return webTarget.request(MediaType.APPLICATION_XML).get(Joueur.class);
    }
}
