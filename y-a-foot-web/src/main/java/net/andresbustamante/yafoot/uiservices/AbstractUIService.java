package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.util.ConstantesWeb;
import net.andresbustamante.yafoot.xs.Contexte;

import javax.faces.context.FacesContext;

/**
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    private Contexte contexte;

    public Contexte getContexte() {
        if (contexte == null) {
            Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(ConstantesWeb.CONTEXTE);
            contexte = (obj != null) ? (Contexte) obj : null;
        }
        return contexte;
    }
}
