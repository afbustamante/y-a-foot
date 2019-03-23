package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import net.andresbustamante.yafoot.web.vo.MatchVO;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author andresbustamante
 */
public class RechercheMatchsJoueurController extends AbstractController {

    private List<MatchVO> matchsAJouer;
    private List<MatchVO> matchsJoues;

    private ListModel<MatchVO> listModelMatchsAJouer;
    private ListModel<MatchVO> listModelMatchsJoues;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Wire
    private Listbox lbxMatchesToPlay;

    @Wire
    private Listbox lbxPlayedMatches;

    @Override
    protected void init() {
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

            listModelMatchsAJouer = new ListModelArray<>(matchsAJouer);
            listModelMatchsJoues = new ListModelArray<>(matchsJoues);

            lbxMatchesToPlay.setModel(listModelMatchsAJouer);
            lbxPlayedMatches.setModel(listModelMatchsJoues);
        } catch (ApplicationException e) {
            // TODO Afficher message d'erreur
        }
    }
}
