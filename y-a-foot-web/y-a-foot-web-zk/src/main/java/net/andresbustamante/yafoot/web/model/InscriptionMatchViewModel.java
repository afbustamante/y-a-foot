package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.enums.MatchJoinModeEnum;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Voiture;
import net.andresbustamante.yafoot.web.services.CarsManagementUIService;
import net.andresbustamante.yafoot.web.services.InscriptionMatchsUIService;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.Collections;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.WebConstants.VOITURES;

public class InscriptionMatchViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(InscriptionMatchViewModel.class);

    private Match match;

    private boolean playerAlreadyListed;

    private MatchJoinModeEnum modeInscription;

    private boolean carProposalEnabled;

    private ListModel<Voiture> carsListModel;

    private Window winNouvelleVoiture;

    private Voiture car;

    @WireVariable
    private InscriptionMatchsUIService inscriptionMatchsUIService;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @WireVariable
    private CarsManagementUIService carsManagementUIService;

    @Init
    @Override
    public void init() {
        try {
            String codeMatch = Executions.getCurrent().getParameter("code");
            match = rechercheMatchsUIService.chercherMatchParCode(codeMatch);

            if (match == null) {
                Messagebox.show(Labels.getLabel("match.join.not.found"),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.ERROR,
                        event -> event.getTarget().detach());
            }

            processJoinMode(Executions.getCurrent().getParameter("mode"));

            List<Inscription> inscriptions = (match.getInscriptions().getInscription() != null) ?
                    match.getInscriptions().getInscription() : Collections.emptyList();

            String nomUtilisateur = getNomUtilisateurActif();
            playerAlreadyListed = false;

            if (CollectionUtils.isNotEmpty(inscriptions) && nomUtilisateur != null) {
                for (Inscription i : inscriptions) {
                    if (nomUtilisateur.equalsIgnoreCase(i.getJoueur().getEmail())) {
                        playerAlreadyListed = true;
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
    @NotifyChange("inscriptionRealisee")
    public void joinMatch() {
        switch (modeInscription) {
            case JOIN_WITH_CAR:
                inscrireJoueurMatch(match, car);
                break;
            case JOIN_WITHOUT_CAR:
                // TODO Implémenter inscription sans voiture
                break;
            default:
                break;
        }
    }

    @Command
    public void showNewCarDialog() {
        winNouvelleVoiture = (Window) Executions.createComponents("/cars/dialog_new.zul", null, null);
        winNouvelleVoiture.setClosable(true);
        winNouvelleVoiture.doModal();
    }

    @GlobalCommand
    @NotifyChange("carsListModel")
    public void refreshCarList() {
        List<Voiture> voitures = (List<Voiture>) Executions.getCurrent().getSession().getAttribute(VOITURES);

        carsListModel = new ListModelArray<>(voitures);

        if (winNouvelleVoiture != null) {
            winNouvelleVoiture.detach();
        }
    }

    public boolean isPlayerAlreadyListed() {
        return playerAlreadyListed;
    }

    public boolean isCarProposalEnabled() {
        return carProposalEnabled;
    }

    public ListModel<Voiture> getCarsListModel() {
        return carsListModel;
    }

    public Voiture getCar() {
        return car;
    }

    public void setCar(Voiture car) {
        this.car = car;
    }

    private void enableCarProposal() {
        carProposalEnabled = true;

        try {
            List<Voiture> cars = carsManagementUIService.findCarsByUser();

            if (cars != null) {
                carsListModel = new ListModelArray<>(cars);
            }
        } catch (ApplicationException e) {
            log.error("Error when loading cars for user", e);
        }
    }

    private void disableCarProposal() {
        carProposalEnabled = false;
        carsListModel = null;
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

    private void processJoinMode(String mode) {
        if (mode != null) {
            modeInscription = MatchJoinModeEnum.valueOf(mode);

            switch (modeInscription) {
                case JOIN_WITH_CAR:
                    enableCarProposal();
                    break;
                case JOIN_WITHOUT_CAR:
                    disableCarProposal();
                    break;
                case JOIN_ONLY:
                    disableCarProposal();
                    inscrireJoueurMatch(match, null);
                    break;
                default:
                    disableCarProposal();
                    break;
            }
        }
    }
}
