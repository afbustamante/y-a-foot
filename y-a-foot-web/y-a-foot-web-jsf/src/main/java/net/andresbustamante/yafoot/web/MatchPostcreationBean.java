package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.util.WebConstants;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class MatchPostcreationBean extends AbstractFacesBean implements Serializable {

    private String codeMatch;

    public String getCodeMatch() {
        Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants
                .CODE_MATCH);

        if (obj != null) {
            codeMatch = (String) obj;
        }
        return codeMatch;
    }
}
