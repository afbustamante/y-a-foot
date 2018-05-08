package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.util.ConstantesWeb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class MatchPostcreationBean {

    private String codeMatch;

    public MatchPostcreationBean() {
    }

    public String getCodeMatch() {
        Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(ConstantesWeb
                .CODE_MATCH);

        if (obj != null) {
            codeMatch = (String) obj;
        }
        return codeMatch;
    }
}
