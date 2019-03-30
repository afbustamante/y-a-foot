package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;

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

    public ListModel<Match> getMatchsAJouerListModel() {
        return matchsAJouerListModel;
    }

    public ListModel<Match> getMatchsJouesListModel() {
        return matchsJouesListModel;
    }

    public int getNbInscriptions(@BindingParam("match") Match match) {
        return 0;
    }
}
