package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.xs.Match;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class MatchSearchBean implements Serializable {

    private String codeMatch;
    private Match match;

    public MatchSearchBean() {
    }

    public void chercherMatch() {
    }

    public String getCodeMatch() {
        return codeMatch;
    }

    public void setCodeMatch(String codeMatch) {
        this.codeMatch = codeMatch;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
