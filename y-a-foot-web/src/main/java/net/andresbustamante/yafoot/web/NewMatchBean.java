package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.uiservices.OrganisationMatchsUIService;
import net.andresbustamante.yafoot.util.ConstantesWeb;
import net.andresbustamante.yafoot.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class NewMatchBean implements Serializable {

    private Date dateMatch;
    private String heureMatch;
    private Integer numMinJoueurs;
    private Integer numMaxJoueurs;
    private Integer idSite;
    private String nomSite;
    private String adresseSite;
    private String telephoneSite;
    private BigDecimal latitudeSite;
    private BigDecimal longitudeSite;
    private boolean optionCovoiturageActive;
    private boolean optionInvitationPubliqueActive;
    private Locale locale;
    private String patternDate;

    private transient final Log log = LogFactory.getLog(NewMatchBean.class);

    @Inject
    private OrganisationMatchsUIService organisationMatchsUIService;

    public NewMatchBean() {
    }

    public Date getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(Date dateMatch) {
        this.dateMatch = dateMatch;
    }

    public String getHeureMatch() {
        return heureMatch;
    }

    public void setHeureMatch(String heureMatch) {
        this.heureMatch = heureMatch;
    }

    public Integer getNumMinJoueurs() {
        return numMinJoueurs;
    }

    public void setNumMinJoueurs(Integer numMinJoueurs) {
        this.numMinJoueurs = numMinJoueurs;
    }

    public Integer getNumMaxJoueurs() {
        return numMaxJoueurs;
    }

    public void setNumMaxJoueurs(Integer numMaxJoueurs) {
        this.numMaxJoueurs = numMaxJoueurs;
    }

    public Integer getIdSite() {
        return idSite;
    }

    public void setIdSite(Integer idSite) {
        this.idSite = idSite;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public String getAdresseSite() {
        return adresseSite;
    }

    public void setAdresseSite(String adresseSite) {
        this.adresseSite = adresseSite;
    }

    public String getTelephoneSite() {
        return telephoneSite;
    }

    public void setTelephoneSite(String telephoneSite) {
        this.telephoneSite = telephoneSite;
    }

    public BigDecimal getLatitudeSite() {
        return latitudeSite;
    }

    public void setLatitudeSite(BigDecimal latitudeSite) {
        this.latitudeSite = latitudeSite;
    }

    public BigDecimal getLongitudeSite() {
        return longitudeSite;
    }

    public void setLongitudeSite(BigDecimal longitudeSite) {
        this.longitudeSite = longitudeSite;
    }

    public boolean isOptionCovoiturageActive() {
        return optionCovoiturageActive;
    }

    public void setOptionCovoiturageActive(boolean optionCovoiturageActive) {
        this.optionCovoiturageActive = optionCovoiturageActive;
    }

    public boolean isOptionInvitationPubliqueActive() {
        return optionInvitationPubliqueActive;
    }

    public void setOptionInvitationPubliqueActive(boolean optionInvitationPubliqueActive) {
        this.optionInvitationPubliqueActive = optionInvitationPubliqueActive;
    }

    public Locale getLocale() {
        if (locale == null) {
            locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        }
        return locale;
    }

    public String getPatternDate() {
        if (patternDate == null) {
            patternDate = DateUtils.getPatternDateHeure(getLocale().getLanguage());
        }
        return patternDate;
    }

    public void setPatternDate(String patternDate) {
        this.patternDate = patternDate;
    }

    /**
     *
     * @return
     */
    public String creerNouveauMatch() {
        log.info("Nouvelle demande de création de match depuis " + getLocale().getDisplayCountry());

        Calendar date = Calendar.getInstance(getLocale());
        date.setTime(dateMatch);

        Site site = new Site();
        site.setId(idSite);
        site.setNom(nomSite);
        site.setAdresse(adresseSite);
        site.setTelephone(telephoneSite);

        Match match = new Match();
        match.setNumJoueursMin(numMinJoueurs);
        match.setNumJoueursMax(numMaxJoueurs);
        match.setDateMatch(date.getTime());
        match.setSite(site);

        try {
            String codeMatch = organisationMatchsUIService.creerMatch(match);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(ConstantesWeb.CODE_MATCH,
                    codeMatch);
        } catch (ApplicationException e) {
            log.error("Erreur lors de la création d'un match", e);
            FacesMessage facesMessage = new FacesMessage();
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesMessage.setSummary(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(e.getMessage(), facesMessage);
        }

        return "match_postcreation";
    }
}
