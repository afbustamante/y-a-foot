package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.web.services.InscriptionMatchsUIService;
import net.andresbustamante.yafoot.web.services.OrganisationMatchsUIService;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author andresbustamante
 */
public class RechercheMatchsJoueurViewModel extends AbstractViewModel {

    private List<Match> matchsAJouer;
    private List<Match> matchsJoues;

    private ListModel<Match> matchsAJouerListModel;
    private ListModel<Match> matchsJouesListModel;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @WireVariable
    private OrganisationMatchsUIService organisationMatchsUIService;

    @WireVariable
    private InscriptionMatchsUIService inscriptionMatchsUIService;

    private final Logger log = LoggerFactory.getLogger(RechercheMatchsJoueurViewModel.class);

    @Init
    public void init() {
        try {
            Matchs matchs = rechercheMatchsUIService.chercherMatchsJoueur();

            matchsJoues = new ArrayList<>();
            matchsAJouer = new ArrayList<>();

            for (Match match : matchs.getMatch()) {
                if (match.getDate().before(Calendar.getInstance(match.getDate().getTimeZone()))) {
                    matchsJoues.add(match);
                } else {
                    matchsAJouer.add(match);
                }
            }

            matchsAJouerListModel = new ListModelArray<>(matchsAJouer);
            matchsJouesListModel = new ListModelArray<>(matchsJoues);
        } catch (ApplicationException e) {
            // TODO Afficher message d'erreur
        }
    }

    @Command
    public void afficherDetailMatch(@BindingParam("match") Match match) {
        // Implémenter cette méthode
    }

    @Command
    @NotifyChange("matchsAJouerListModel")
    public void quitterMatch(@BindingParam("match") Match match) {
        EventListener<Messagebox.ClickEvent> clickListener = event -> {
            if (Messagebox.Button.YES.equals(event.getButton())) {
                try {
                    inscriptionMatchsUIService.desinscrireJoueurMatch(match);
                    Messagebox.show(Labels.getLabel("match.list.leave.success"),
                            Labels.getLabel(DIALOG_CONFIRMATION_TITLE),
                            Messagebox.Button.OK.id,
                            Messagebox.INFORMATION);
                    init();
                } catch (ApplicationException e) {
                    log.error("Erreur lors de l'abandon d'un match", e);
                    Messagebox.show(Labels.getLabel("match.list.leave.error"),
                            Labels.getLabel(DIALOG_ERROR_TITLE),
                            Messagebox.Button.OK.id,
                            Messagebox.ERROR);
                }
            }
        };
        Messagebox.show(Labels.getLabel("match.list.leave.warning"),
                Labels.getLabel(DIALOG_CONFIRMATION_TITLE),
                new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO},
                Messagebox.QUESTION, clickListener);
    }

    @Command
    @NotifyChange("matchsAJouerListModel")
    public void annulerMatch(@BindingParam("match") Match match) {
        if (isAnnulationPossible(match)) {
            // Afficher message de confirmation pour l'annulation
            EventListener<Messagebox.ClickEvent> clickListener = event -> {
                if (Messagebox.Button.YES.equals(event.getButton())) {
                    try {
                        organisationMatchsUIService.annulerMatch(match);
                        Messagebox.show(Labels.getLabel("match.list.cancel.success"),
                                Labels.getLabel(DIALOG_CONFIRMATION_TITLE),
                                Messagebox.Button.OK.id,
                                Messagebox.INFORMATION);
                    } catch (ApplicationException e) {
                        log.error("Erreur lors de l'annulation d'un match", e);
                        Messagebox.show(Labels.getLabel("match.list.cancel.error"),
                                Labels.getLabel(DIALOG_ERROR_TITLE),
                                Messagebox.Button.OK.id,
                                Messagebox.ERROR);
                    }
                }
            };
            Messagebox.show(Labels.getLabel("match.list.cancel.warning"),
                    Labels.getLabel(DIALOG_CONFIRMATION_TITLE),
                    new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO},
                    Messagebox.QUESTION, clickListener);
        } else {
            // Afficher message informatif sur les conditions d'annulation et terminer
            Messagebox.show(Labels.getLabel("match.list.cancel.not.allowed"),
                    Labels.getLabel(DIALOG_INFORMATION_TITLE), Messagebox.Button.OK.id,
                    Messagebox.INFORMATION);
        }
    }

    public ListModel<Match> getMatchsAJouerListModel() {
        return matchsAJouerListModel;
    }

    public ListModel<Match> getMatchsJouesListModel() {
        return matchsJouesListModel;
    }

    private boolean isAnnulationPossible(Match match) {
        return getNomUtilisateurActif().equals(match.getCreateur().getEmail());
    }
}
