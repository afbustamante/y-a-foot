package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import net.andresbustamante.yafoot.web.vo.MatchVO;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
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

    private List<MatchVO> matchsAJouer;
    private List<MatchVO> matchsJoues;

    private ListModel<MatchVO> matchsAJouerListModel;
    private ListModel<MatchVO> matchsJouesListModel;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Init
    public void init() {
        try {
            Matchs matchs = rechercheMatchsUIService.chercherMatchsJoueur();

            matchsJoues = new ArrayList<>();
            matchsAJouer = new ArrayList<>();

            for (Match match : matchs.getMatch()) {
                MatchVO matchVO = MatchMapper.INSTANCE.toMatchVO(match);
                matchVO.setTexteDate(getDateFormat().format(match.getDate().getTime()));
                matchVO.setTexteJoueursAttendus(Labels.getLabel("match.list.players.expected",
                        new String[]{match.getNumJoueursMin().toString()}));
                matchVO.setTexteJoueursActuels(Labels.getLabel("match.list.players.actual",
                        new String[]{matchVO.getNumJoueursInscrits().toString()}));

                if (match.getDate().before(Calendar.getInstance(match.getDate().getTimeZone()))) {
                    matchsJoues.add(matchVO);
                } else {
                    matchsAJouer.add(matchVO);
                }
            }

            matchsAJouerListModel = new ListModelArray<>(matchsAJouer);
            matchsJouesListModel = new ListModelArray<>(matchsJoues);
        } catch (ApplicationException e) {
            // TODO Afficher message d'erreur
        }
    }

    public ListModel<MatchVO> getMatchsAJouerListModel() {
        return matchsAJouerListModel;
    }

    public ListModel<MatchVO> getMatchsJouesListModel() {
        return matchsJouesListModel;
    }
}
