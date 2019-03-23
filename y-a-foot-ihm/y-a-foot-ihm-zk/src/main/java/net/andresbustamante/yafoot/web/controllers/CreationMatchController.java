package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.web.services.OrganisationMatchsUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.Calendar;
import java.util.*;

import static net.andresbustamante.yafoot.web.ConstantesWeb.PAGE_LISTE_MATCHS;

/**
 * @author andresbustamante
 */
public class CreationMatchController extends AbstractController {

    private static final long serialVersionUID = 1L;
    private final transient Logger log = LoggerFactory.getLogger(CreationMatchController.class);

    @Wire
    private Datebox dtbDateMatch;

    @Wire
    private Spinner spbNbMinJoueurs;

    @Wire
    private Spinner spbNbMaxJoueurs;

    @Wire
    private Combobox cbbSite;

    @WireVariable
    private transient OrganisationMatchsUIService organisationMatchsUIService;

    private List<Site> sites;

    private Map<Integer, Site> mapSites;

    private ListModel<Site> sitesModel;

    @Override
    protected void init() {
        try {
            sites = organisationMatchsUIService.chercherSites();

            if (sites != null) {
                sitesModel = new ListModelArray<>(sites);
                cbbSite.setModel(sitesModel);
                mapSites = new TreeMap<>();

                for (Site site : sites) {
                    mapSites.put(site.getId(), site);
                }
            }
        } catch (ApplicationException e) {
            // TODO Afficher message d'erreur
        }
    }

    @Listen(Events.ON_CLICK + " = #btnNouveauSite")
    public void afficherDialogCreationSite() {
        Map<String, Object> arguments = new WeakHashMap<>();
        arguments.put("sitesModel", sitesModel);
        String template = "/sites/new_site_dialog.zul";
        Window window = (Window) Executions.createComponents(template, this.getSelf(), arguments);
        window.doModal();
    }

    @Listen(Events.ON_CLICK + " = #btnContinue")
    public void creerMatch() {
        try {
            Calendar dateMatch = Calendar.getInstance();
            dateMatch.setTime(dtbDateMatch.getValue());

            Match match = new Match();
            match.setDate(dateMatch);
            match.setNumJoueursMin(spbNbMinJoueurs.getValue());
            match.setNumJoueursMax(spbNbMaxJoueurs.getValue());
            match.setSite(mapSites.get(Integer.valueOf(cbbSite.getSelectedItem().getValue())));

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
}
