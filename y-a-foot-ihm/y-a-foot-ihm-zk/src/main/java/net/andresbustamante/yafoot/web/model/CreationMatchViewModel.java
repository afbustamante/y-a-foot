package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.web.services.OrganisationMatchsUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

import static net.andresbustamante.yafoot.web.ConstantesWeb.PAGE_LISTE_MATCHS;

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

    private List<Site> sites;

    private Map<Integer, Site> mapSites;

    private ListModel<Site> sitesListModel;

    @Init
    public void init() {
        try {
            sites = organisationMatchsUIService.chercherSites();

            if (sites != null) {
                sitesListModel = new ListModelArray<>(sites);
                mapSites = new TreeMap<>();

                for (Site site : sites) {
                    mapSites.put(site.getId(), site);
                }
            }
        } catch (ApplicationException e) {
            // TODO Afficher message d'erreur
        }
    }

    @Command
    public void afficherDialogCreationSite() {
        Map<String, Object> arguments = new WeakHashMap<>();
        arguments.put("sitesListModel", sitesListModel);
        String template = "/sites/new_site_dialog.zul";
        Window window = (Window) Executions.createComponents(template, null, arguments);
        window.doModal();
    }

    @Command
    public void creerMatch() {
        try {
            Calendar dateMatch = Calendar.getInstance();
            dateMatch.setTime(date);

            Match match = new Match();
            match.setDate(dateMatch);
            match.setNumJoueursMin(nbMinJoueurs);
            match.setNumJoueursMax(nbMaxJoueurs);
            match.setSite(site);

            String codeMatch = organisationMatchsUIService.creerMatch(match);

            EventListener<Messagebox.ClickEvent> clickListener = event -> Executions.sendRedirect(PAGE_LISTE_MATCHS);
            Messagebox.show(Labels.getLabel("new.match.success", new String[]{codeMatch}),
                    Labels.getLabel("dialog.information.title"),
                    new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION, clickListener);
        } catch (ApplicationException e) {
            log.error("Erreur lors de la création d'un match", e);
            Clients.showNotification(Labels.getLabel("application.exception.text", e.getMessage()), true);
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
}
