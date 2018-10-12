package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.uiservices.RechercheMatchsUIService;
import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.util.MessagesProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Locale;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class MatchSearchBean implements Serializable {

    private String codeMatch;
    private Match match;
    private String complementJoueurs;
    private Locale locale;
    private String patternDate;
    private transient final Log log = LogFactory.getLog(MatchSearchBean.class);

    @Inject
    private RechercheMatchsUIService rechercheMatchsUIService;

    public MatchSearchBean() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public void chercherMatch() {
        log.info("Recherche du match avec le code " + codeMatch);
        match = rechercheMatchsUIService.getMatchParCode(codeMatch);
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

    public String getComplementJoueurs() {
        Integer numPlacesDisponibles = getNumPlacesDisponibles();

        if (numPlacesDisponibles != null)
            complementJoueurs = MessagesProperties.getValue("match.search.result.spots.left", locale,
                    getNumPlacesDisponibles());
        return complementJoueurs;
    }

    public void setComplementJoueurs(String complementJoueurs) {
        this.complementJoueurs = complementJoueurs;
    }

    private Integer getNumPlacesDisponibles() {
        if ((match != null) && (match.getNumJoueursMax() != null) && (match.getInscriptions() != null)) {
            return match.getNumJoueursMax() - match.getInscriptions().getInscription().size();
        }
        return null;
    }

    public String getPatternDate() {
        if (patternDate == null) {
            patternDate = DateUtils.getPatternDateHeure(locale.getLanguage());
        }
        return patternDate;
    }

    public void setPatternDate(String patternDate) {
        this.patternDate = patternDate;
    }
}
