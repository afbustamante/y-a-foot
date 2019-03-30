package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;

import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
public class RechercheMatchViewModel extends AbstractViewModel {

    private final transient Logger log = LoggerFactory.getLogger(RechercheMatchViewModel.class);

    private Match match;

    private String code;

    private boolean matchFound;

    private int nbPlacesDisponibles;

    private ListModel<Inscription> inscriptionsListModel;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Init
    public void init() {
        matchFound = false;
    }

    @Command
    @NotifyChange({"matchFound", "match", "inscriptionsListModel"})
    public void chercherMatch() {
        try {
            match = rechercheMatchsUIService.chercherMatchParCode(code);

            if (match != null) {
                matchFound = true;

                List<Inscription> inscriptions = (match.getInscriptions().getInscription() != null) ?
                        match.getInscriptions().getInscription() : Collections.emptyList();
                inscriptionsListModel = new ListModelArray<>(inscriptions);
            } else {
                matchFound = false;
                inscriptionsListModel = new ListModelArray<>(Collections.emptyList());
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la recherche d'un match", e);
        }
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
        if (nbPlacesDisponibles == 0) {
            if (match != null && match.getInscriptions() != null) {
                nbPlacesDisponibles = Math.max(0, match.getNumJoueursMax() - match.getInscriptions().getInscription().size());
            }
        }
        return nbPlacesDisponibles;
    }
}
