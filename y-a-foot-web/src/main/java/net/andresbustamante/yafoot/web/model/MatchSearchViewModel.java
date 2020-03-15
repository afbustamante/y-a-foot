package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Registration;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.services.MatchsSearchUIService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Window;

import java.util.Collections;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.WebConstants.MATCH_JOIN_MODE;

/**
 * @author andresbustamante
 */
public class MatchSearchViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(MatchSearchViewModel.class);

    private Match match;

    private String code;

    private boolean matchFound;

    private boolean registrationPossible;

    private int numPlayersLeft;

    private ListModel<Registration> registrationsListModel;

    private Window winJoinMatch;

    @WireVariable
    private MatchsSearchUIService matchsSearchUIService;

    @Init
    public void init() {
        matchFound = false;
    }

    @Command
    @NotifyChange({"matchFound", "match", "registrationsListModel", "registrationPossible"})
    public void findMatch() {
        try {
            match = matchsSearchUIService.findMatchByCode(code);

            if (match != null) {
                matchFound = true;
                boolean dejaInscrit = false;

                List<Registration> registrations = (match.getRegistrations() != null) ?
                        match.getRegistrations() : Collections.emptyList();

                String nomUtilisateur = getActiveUsername();

                dejaInscrit = isPlayerAlredyRegistered(nomUtilisateur, registrations);

                registrationPossible = matchFound && !dejaInscrit;

                registrationsListModel = new ListModelArray<>(registrations);
            } else {
                matchFound = false;
                registrationsListModel = new ListModelArray<>(Collections.emptyList());
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la recherche d'un match", e);
        }
    }

    @Command
    public void showMatchJoinDialog() {
        winJoinMatch = (Window) Executions.createComponents("/matches/dialog_join.zul", null, null);
        winJoinMatch.setClosable(true);
        winJoinMatch.doModal();
    }

    @GlobalCommand
    public void showMatchJoinPage() {
        if (winJoinMatch != null) {
            winJoinMatch.detach();
        }

        String mode = "";

        if (Executions.getCurrent().getSession().hasAttribute(MATCH_JOIN_MODE)) {
            mode = "&mode=" + Executions.getCurrent().getSession().getAttribute(MATCH_JOIN_MODE);
        }

        Executions.getCurrent().sendRedirect("/matches/join.zul?code=" + match.getCode() + mode);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Match getMatch() {
        return match;
    }

    public boolean isMatchFound() {
        return matchFound;
    }

    public ListModel<Registration> getRegistrationsListModel() {
        return registrationsListModel;
    }

    public int getNumPlayersLeft() {
        if ((numPlayersLeft == 0) && (match != null) && (match.getRegistrations() != null)) {
            numPlayersLeft = Math.max(0, match.getNumPlayersMax() - match.getRegistrations().size());
        }
        return numPlayersLeft;
    }

    public boolean isRegistrationPossible() {
        return registrationPossible;
    }

    private boolean isPlayerAlredyRegistered(String username, List<Registration> registrations) {
        if (CollectionUtils.isNotEmpty(registrations) && username != null) {
            for (Registration i : registrations) {
                if (username.equalsIgnoreCase(i.getPlayer().getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }
}
