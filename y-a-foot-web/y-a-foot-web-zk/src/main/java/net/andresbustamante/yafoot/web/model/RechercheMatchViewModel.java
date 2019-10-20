package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
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
public class RechercheMatchViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(RechercheMatchViewModel.class);

    private Match match;

    private String code;

    private boolean matchFound;

    private boolean inscriptionPossible;

    private int nbPlacesDisponibles;

    private ListModel<Inscription> inscriptionsListModel;

    private Window winJoinMatch;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Init
    public void init() {
        matchFound = false;
    }

    @Command
    @NotifyChange({"matchFound", "match", "inscriptionsListModel", "inscriptionPossible"})
    public void chercherMatch() {
        try {
            match = rechercheMatchsUIService.chercherMatchParCode(code);

            if (match != null) {
                matchFound = true;
                boolean dejaInscrit = false;

                List<Inscription> inscriptions = (match.getInscriptions().getInscription() != null) ?
                        match.getInscriptions().getInscription() : Collections.emptyList();

                String nomUtilisateur = getNomUtilisateurActif();

                dejaInscrit = isJoueurDejaInscrit(nomUtilisateur, inscriptions);

                inscriptionPossible = matchFound && !dejaInscrit;

                inscriptionsListModel = new ListModelArray<>(inscriptions);
            } else {
                matchFound = false;
                inscriptionsListModel = new ListModelArray<>(Collections.emptyList());
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

    public ListModel<Inscription> getInscriptionsListModel() {
        return inscriptionsListModel;
    }

    public int getNbPlacesDisponibles() {
        if ((nbPlacesDisponibles == 0) && (match != null) && (match.getInscriptions() != null)) {
            nbPlacesDisponibles = Math.max(0, match.getNbJoueursMax() - match.getInscriptions().getInscription().size());
        }
        return nbPlacesDisponibles;
    }

    public boolean isInscriptionPossible() {
        return inscriptionPossible;
    }

    private boolean isJoueurDejaInscrit(String nomUtilisateur, List<Inscription> inscriptions) {
        if (CollectionUtils.isNotEmpty(inscriptions) && nomUtilisateur != null) {
            for (Inscription i : inscriptions) {
                if (nomUtilisateur.equalsIgnoreCase(i.getJoueur().getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }
}
