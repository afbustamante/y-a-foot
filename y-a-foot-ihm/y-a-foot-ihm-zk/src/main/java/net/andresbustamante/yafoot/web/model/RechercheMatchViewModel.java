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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author andresbustamante
 */
public class RechercheMatchViewModel extends AbstractViewModel {

    private final transient Logger log = LoggerFactory.getLogger(RechercheMatchViewModel.class);

    private Match match;

    private String code;

    private boolean matchFound;

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
            Match match = rechercheMatchsUIService.chercherMatchParCode(code);

            if (match != null) {
                Locale locale = getLocaleUtilisateur();
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
                DateFormat heureFormat = new SimpleDateFormat("H:mm z");

                String texteDate = dateFormat.format(match.getDate().getTime()) + NL +
                        heureFormat.format(match.getDate().getTime());

                matchFound = true;

                List<Inscription> inscriptions = (match.getInscriptions().getInscription() != null) ?
                        match.getInscriptions().getInscription() : Collections.emptyList();
                inscriptionsListModel = new ListModelArray<>(inscriptions);
            } else {
                matchFound = false;
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
}
