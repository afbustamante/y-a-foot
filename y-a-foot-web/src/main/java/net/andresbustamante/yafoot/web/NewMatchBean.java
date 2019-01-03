package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.uiservices.OrganisationMatchsUIService;
import net.andresbustamante.yafoot.util.ConstantesWeb;
import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.util.MessagesProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

import static net.andresbustamante.yafoot.security.Roles.JOUEUR;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class NewMatchBean implements Serializable {

    private static final Integer NOUVEL_ID = -1;

    private Date maintenant;
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
    private List<SelectItem> itemsSites;
    private boolean nouveauSite;

    private transient final Log log = LogFactory.getLog(NewMatchBean.class);

    @Inject
    private OrganisationMatchsUIService organisationMatchsUIService;

    public NewMatchBean() {
        nouveauSite = false;
    }

    public Date getMaintenant() {
        if (maintenant == null) {
            // TODO Obtenir la date en fonction de la TZ de l'utilisateur
            maintenant = Calendar.getInstance(getLocale()).getTime();
            return maintenant;
        }
        return maintenant;
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
            patternDate = DateUtils.getPatternDate(getLocale().getLanguage());
        }
        return patternDate;
    }

    public void setPatternDate(String patternDate) {
        this.patternDate = patternDate;
    }

    @RolesAllowed(JOUEUR)
    public List<SelectItem> getItemsSites() {
        if (itemsSites == null) {
            itemsSites = new ArrayList<>();

            try {
                List<Site> sites = organisationMatchsUIService.chercherSites();

                if (CollectionUtils.isNotEmpty(sites)) {
                    for (Site site : sites) {
                        itemsSites.add(new SelectItem(site.getId(), site.getNom(), site.getAdresse()));
                    }
                }
            } catch (ApplicationException e) {
                FacesMessage facesMessage = new FacesMessage();
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesMessage.setSummary(e.getMessage());
                FacesContext.getCurrentInstance().addMessage(e.getMessage(), facesMessage);
            }
        }
        return itemsSites;
    }

    public boolean isNouveauSite() {
        return nouveauSite;
    }

    public void afficherDialogNouveauSite(ActionEvent event) {
        nouveauSite = true;
    }

    public void ajouterNouveauSite(ActionEvent event) {
        SelectItem nouvelItemSite = new SelectItem(NOUVEL_ID, nomSite, adresseSite);
        getItemsSites().add(nouvelItemSite);
        fermerDialogNouveauSite(event);
    }

    public void fermerDialogNouveauSite(ActionEvent event) {
        nouveauSite = false;
    }

    /**
     *
     * @return
     */
    @RolesAllowed(JOUEUR)
    public String creerNouveauMatch() {
        log.info("Nouvelle demande de création de match");

        if (idSite == null ) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    MessagesProperties.getValue("new.match.place.required", getLocale()), null);
            FacesContext.getCurrentInstance().addMessage("place", facesMessage);
            return null;
        }

        String[] heureMinute = heureMatch.split(DateUtils.SEPARATEUR_HEURE);
        LocalTime heure = LocalTime.of(Integer.valueOf(heureMinute[0]), Integer.valueOf(heureMinute[1]));

        Calendar date = Calendar.getInstance(getLocale());
        date.setTime(dateMatch);
        date.set(Calendar.HOUR_OF_DAY, heure.getHour());
        date.set(Calendar.MINUTE, heure.getMinute());

        Site site = new Site();
        site.setId(idSite);
        site.setNom(nomSite);
        site.setAdresse(adresseSite);
        site.setNumeroTelephone(telephoneSite);

        Match match = new Match();
        match.setNumJoueursMin(numMinJoueurs);
        match.setNumJoueursMax(numMaxJoueurs);
        match.setDate(date);
        match.setSite(site);
        match.setCovoiturageActif(optionCovoiturageActive);
        match.setPartageActif(optionInvitationPubliqueActive);

        try {
            String codeMatch = organisationMatchsUIService.creerMatch(match);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(ConstantesWeb.CODE_MATCH,
                    codeMatch);
            return ConstantesWeb.SUCCES;
        } catch (ApplicationException e) {
            log.error("Erreur lors de la création d'un match", e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), e.getMessage());
            FacesContext.getCurrentInstance().addMessage(e.getMessage(), facesMessage);
            return null;
        }
    }
}
