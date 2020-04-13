package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.dto.Site;
import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.web.services.MatchsRegistryUIService;
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

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.WebConstants.SITES;

/**
 * @author andresbustamante
 */
public class MatchRegistryViewModel extends AbstractViewModel {

    private Date date;

    private Integer numPlayersMin;

    private Integer numPlayersMax;

    private Site site;

    private final Logger log = LoggerFactory.getLogger(MatchRegistryViewModel.class);

    @WireVariable
    private MatchsRegistryUIService matchsRegistryUIService;

    private ListModel<Site> sitesListModel;

    private String matchCode;

    private boolean siteRegistryEnabled;

    private Window winNouveauSite;

    @Init
    public void init() {
        try {
            List<Site> sites = matchsRegistryUIService.findSites();

            if (sites != null) {
                sitesListModel = new ListModelArray<>(sites);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des sites sur l'écran de création des matchs", e);
        }
    }

    @Command
    @NotifyChange("siteRegistryEnabled")
    public void enableSiteRegistry() {
        siteRegistryEnabled = true;

        winNouveauSite = (Window) Executions.createComponents("/sites/dialog_new.zul", null, null);
        winNouveauSite.setClosable(true);
        winNouveauSite.doModal();
    }

    @GlobalCommand
    @NotifyChange("sitesListModel")
    public void refreshSitesList() {
        List<Site> sites = (List<Site>) Executions.getCurrent().getSession().getAttribute(SITES);
        sitesListModel = new ListModelArray<>(sites);

        if (winNouveauSite != null) {
            winNouveauSite.detach();
        }
    }

    @Command
    @NotifyChange("matchCode")
    public void saveMatch() {
        try {
            if (isMatchRegistryImpossible()) {
                return;
            }

            OffsetDateTime matchDate = OffsetDateTime.from(DateUtils.toLocalDateTime(date));

            Match match = new Match();
            match.setDate(matchDate);
            match.setNumPlayersMin(numPlayersMin);
            match.setNumPlayersMax(numPlayersMax);
            match.setSite(site);

            matchCode = matchsRegistryUIService.saveMatch(match);

            if (Executions.getCurrent().getSession().hasAttribute(SITES)) {
                // Un site a été ajouté. Nettoyer la session
                Executions.getCurrent().getSession().removeAttribute(SITES);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la création d'un match", e);
            Messagebox.show(Labels.getLabel("application.exception.text", new String[]{e.getMessage()}),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNumPlayersMin() {
        return numPlayersMin;
    }

    public void setNumPlayersMin(Integer numPlayersMin) {
        this.numPlayersMin = numPlayersMin;
    }

    public Integer getNumPlayersMax() {
        return numPlayersMax;
    }

    public void setNumPlayersMax(Integer numPlayersMax) {
        this.numPlayersMax = numPlayersMax;
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

    public String getMatchCode() {
        return matchCode;
    }

    public boolean isSiteRegistryEnabled() {
        return siteRegistryEnabled;
    }

    public boolean isMatchRegistryImpossible() {
        return (date == null || numPlayersMin == null || site == null);
    }
}
