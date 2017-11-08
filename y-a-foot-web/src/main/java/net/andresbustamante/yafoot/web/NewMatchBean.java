package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.util.DateUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    private String nomSite;
    private String adresseSite;
    private String telephoneSite;
    private BigDecimal latitudeSite;
    private BigDecimal longitudeSite;
    private boolean optionCovoiturageActive;
    private boolean optionInvitationPubliqueActive;
    private String locale;
    private String patternDate;

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

    public String getLocale() {
        if (locale == null) {
            locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage();
        }
        return locale;
    }

    public String getPatternDate() {
        if (patternDate == null) {
            patternDate = DateUtils.getPatternDateHeure(getLocale());
        }
        return patternDate;
    }

    public void setPatternDate(String patternDate) {
        this.patternDate = patternDate;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void creerNouveauMatch() {
    }
}
