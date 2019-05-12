package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.enums.ModeInscriptionMatchEnum;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Voiture;
import net.andresbustamante.yafoot.web.services.InscriptionMatchsUIService;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.Collections;
import java.util.List;

public class InscriptionMatchViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(InscriptionMatchViewModel.class);

    private String codeMatch;

    private Match match;

    private boolean joueurDejaInscrit;

    private String modeInscription;

    @WireVariable
    private InscriptionMatchsUIService inscriptionMatchsUIService;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Init
    @Override
    public void init() {
        try {
            codeMatch = Executions.getCurrent().getParameter("code");
            match = rechercheMatchsUIService.chercherMatchParCode(codeMatch);

            List<Inscription> inscriptions = (match.getInscriptions().getInscription() != null) ?
                    match.getInscriptions().getInscription() : Collections.emptyList();

            String nomUtilisateur = getNomUtilisateurActif();
            joueurDejaInscrit = false;

            if (CollectionUtils.isNotEmpty(inscriptions) && nomUtilisateur != null) {
                for (Inscription i : inscriptions) {
                    if (nomUtilisateur.equalsIgnoreCase(i.getJoueur().getEmail())) {
                        joueurDejaInscrit = true;
                        break;
                    }
                }
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la charge d'un match", e);
            Messagebox.show(Labels.getLabel("match.join.error.detail.text"),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.ERROR,
                    event -> event.getTarget().detach());
        }
    }

    @Command
    public void choisirMode(@BindingParam("mode") String mode) {
        this.modeInscription = mode;
    }

    @Command
    @NotifyChange("inscriptionRealisee")
    public void joindreMatch() {
        ModeInscriptionMatchEnum mode = ModeInscriptionMatchEnum.valueOf(modeInscription);

        switch (mode) {
            case INSCRIPTION_AVEC_VOITURE:
                // TODO Implémenter inscription avec voiture
                break;
            case INSCRIPTION_SANS_VOITURE:
                // TODO Implémenter inscription sans voiture
                break;
            case INSCRIPTION_AUTRE_TRANSPORT:
                inscrireJoueurMatch(match, null);
                break;
            default:
                break;
        }
    }

    public String getModeInscription() {
        return modeInscription;
    }

    public void setModeInscription(String modeInscription) {
        this.modeInscription = modeInscription;
    }

    public boolean isJoueurDejaInscrit() {
        return joueurDejaInscrit;
    }

    private void inscrireJoueurMatch(Match match, Voiture voiture) {
        try {
            inscriptionMatchsUIService.inscrireJoueurMatch(match, voiture);

            Messagebox.show(Labels.getLabel("match.join.success.detail.text"),
                    Labels.getLabel(DIALOG_INFORMATION_TITLE),
                    new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/matches/list.zul"));
        } catch (ApplicationException e) {
            log.error("Erreur lors de l'inscription d'un joueur à un match", e);
            Messagebox.show(Labels.getLabel("match.join.error.detail.text"),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.ERROR,
                    event -> event.getTarget().detach());
        }
    }
}
