package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.util.ConstantesWeb;
import net.andresbustamante.yafoot.xs.Contexte;
import net.andresbustamante.yafoot.xs.UtilisateurType;

import javax.faces.context.FacesContext;
import java.security.Principal;

/**
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    private Contexte contexte;

    public Contexte getContexte() {
        if (contexte == null) {
            Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(ConstantesWeb.CONTEXTE);
            if (obj != null) {
                contexte = (Contexte) obj;
            } else {
                Principal user = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

                if (user != null) {
                    String email = user.getName();
                    contexte = new Contexte();
                    UtilisateurType utilisateur = new UtilisateurType();
                    utilisateur.setEmail(email);
                    contexte.setUtilisateur(utilisateur);
                }
            }
        }
        return contexte;
    }
}
