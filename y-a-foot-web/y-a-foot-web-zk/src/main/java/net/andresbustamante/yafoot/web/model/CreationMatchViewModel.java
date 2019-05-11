package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.web.services.OrganisationMatchsUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author andresbustamante
 */
public class CreationMatchViewModel extends AbstractViewModel {

    private Date date;

    private Integer nbMinJoueurs;

    private Integer nbMaxJoueurs;

    private Site site;

    private final transient Logger log = LoggerFactory.getLogger(CreationMatchViewModel.class);

    @WireVariable
    private transient OrganisationMatchsUIService organisationMatchsUIService;

    private ListModel<Site> sitesListModel;

    private String codeMatch;

    private boolean creationSiteActive;

    private Window winNouveauSite;

    @Init
    public void init() {
        try {
            List<Site> sites = organisationMatchsUIService.chercherSites();

            if (sites != null) {
                sitesListModel = new ListModelArray<>(sites);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des sites sur l'écran de création des matchs", e);
        }
    }

    @Command
    @NotifyChange("creationSiteActive")
    public void activerCreationSite() {
        creationSiteActive = true;

        winNouveauSite = (Window) Executions.createComponents("/sites/dialog_new.zul", null, null);
        winNouveauSite.setClosable(true);
        winNouveauSite.doModal();
    }

    @GlobalCommand
    @NotifyChange("sitesListModel")
    public void rafraichirListeSites() {
        List<Site> sites = (List<Site>) Executions.getCurrent().getSession().getAttribute("sites");
        sitesListModel = new ListModelArray<>(sites);

        if (winNouveauSite != null) {
            winNouveauSite.detach();
        }
    }

    @Command
    @NotifyChange("codeMatch")
    public void creerMatch() {
        try {
            if (isCreationMatchImpossible()) {
                return;
            }

            Calendar dateMatch = Calendar.getInstance();
            dateMatch.setTime(date);

            Match match = new Match();
            match.setDate(dateMatch);
            match.setNumJoueursMin(nbMinJoueurs);
            match.setNumJoueursMax(nbMaxJoueurs);
            match.setSite(site);

            codeMatch = organisationMatchsUIService.creerMatch(match);

            if (Executions.getCurrent().getSession().hasAttribute("sites")) {
                // Un site a été ajouté. Nettoyer la session
                Executions.getCurrent().getSession().removeAttribute("sites");
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la création d'un match", e);
            Messagebox.show(Labels.getLabel("application.exception.text", new String[]{e.getMessage()}),
                    Labels.getLabel("dialog.error.title"),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNbMinJoueurs() {
        return nbMinJoueurs;
    }

    public void setNbMinJoueurs(Integer nbMinJoueurs) {
        this.nbMinJoueurs = nbMinJoueurs;
    }

    public Integer getNbMaxJoueurs() {
        return nbMaxJoueurs;
    }

    public void setNbMaxJoueurs(Integer nbMaxJoueurs) {
        this.nbMaxJoueurs = nbMaxJoueurs;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ListModel<Site> getSitesListModel() {
        return sitesListModel;
    }

    public String getCodeMatch() {
        return codeMatch;
    }

    public boolean isCreationSiteActive() {
        return creationSiteActive;
    }

    public boolean isCreationMatchImpossible() {
        return (date == null || nbMinJoueurs == null || site == null);
    }
}