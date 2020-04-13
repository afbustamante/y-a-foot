package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.services.MatchsJoiningUIService;
import net.andresbustamante.yafoot.web.services.MatchsRegistryUIService;
import net.andresbustamante.yafoot.web.services.MatchsSearchUIService;
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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andresbustamante
 */
public class PlayersMatchsListingViewModel extends AbstractViewModel {

    private ListModel<Match> matchesToPlayListModel;
    private ListModel<Match> matchesPlayedListModel;

    @WireVariable
    private MatchsSearchUIService matchsSearchUIService;

    @WireVariable
    private MatchsRegistryUIService matchsRegistryUIService;

    @WireVariable
    private MatchsJoiningUIService matchsJoiningUIService;

    private final Logger log = LoggerFactory.getLogger(PlayersMatchsListingViewModel.class);

    @Init
    public void init() {
        try {
            List<Match> matches = matchsSearchUIService.findMatchesForPlayer();

            if (matches == null) {
                matches = new ArrayList<>();
            }

            List<Match> matchesPlayed = new ArrayList<>();
            List<Match> matchesToPlay = new ArrayList<>();

            for (Match match : matches) {
                if (match.getDate().isBefore(OffsetDateTime.now().minusHours(2))) {
                    matchesPlayed.add(match);
                } else {
                    matchesToPlay.add(match);
                }
            }

            matchesToPlayListModel = new ListModelArray<>(matchesToPlay);
            matchesPlayedListModel = new ListModelArray<>(matchesPlayed);
        } catch (ApplicationException e) {
            // TODO Afficher message d'erreur
        }
    }

    @Command
    public void showMatchDetail(@BindingParam("match") Match match) {
        // Implémenter cette méthode
    }

    @Command
    @NotifyChange("matchesToPlayListModel")
    public void leaveMatch(@BindingParam("match") Match match) {
        EventListener<Messagebox.ClickEvent> clickListener = event -> {
            if (Messagebox.Button.YES.equals(event.getButton())) {
                try {
                    matchsJoiningUIService.unregisterPlayerFromMatch(match);
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
    @NotifyChange("matchesToPlayListModel")
    public void cancelMatch(@BindingParam("match") Match match) {
        if (isCancellingPossible(match)) {
            // Afficher message de confirmation pour l'annulation
            EventListener<Messagebox.ClickEvent> clickListener = event -> {
                if (Messagebox.Button.YES.equals(event.getButton())) {
                    try {
                        matchsRegistryUIService.cancelMatch(match);
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

    public ListModel<Match> getMatchesToPlayListModel() {
        return matchesToPlayListModel;
    }

    public ListModel<Match> getMatchesPlayedListModel() {
        return matchesPlayedListModel;
    }

    private boolean isCancellingPossible(Match match) {
        return getActiveUsername().equals(match.getAuthor().getEmail());
    }
}
