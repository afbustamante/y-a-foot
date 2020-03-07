package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Car;
import net.andresbustamante.yafoot.model.xs.Registration;
import net.andresbustamante.yafoot.web.enums.MatchJoinModeEnum;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.web.services.CarsManagementUIService;
import net.andresbustamante.yafoot.web.services.MatchsJoiningUIService;
import net.andresbustamante.yafoot.web.services.MatchsSearchUIService;
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

public class MatchJoiningViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(MatchJoiningViewModel.class);

    private Match match;

    private boolean playerAlreadyListed;

    private MatchJoinModeEnum modeInscription;

    private boolean carProposalEnabled;

    private ListModel<Car> carsListModel;

    private Window winNouvelleVoiture;

    private Car car;

    @WireVariable
    private MatchsJoiningUIService matchsJoiningUIService;

    @WireVariable
    private MatchsSearchUIService matchsSearchUIService;

    @WireVariable
    private CarsManagementUIService carsManagementUIService;

    @Init
    @Override
    public void init() {
        try {
            String codeMatch = Executions.getCurrent().getParameter("code");
            match = matchsSearchUIService.findMatchByCode(codeMatch);

            if (match == null) {
                Messagebox.show(Labels.getLabel("match.join.not.found"),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.ERROR,
                        event -> event.getTarget().detach());
            }

            processJoinMode(Executions.getCurrent().getParameter("mode"));

            List<Registration> registrations = (match.getRegistrations().getRegistration() != null) ?
                    match.getRegistrations().getRegistration() : Collections.emptyList();

            String nomUtilisateur = getActiveUsername();
            playerAlreadyListed = false;

            if (CollectionUtils.isNotEmpty(registrations) && nomUtilisateur != null) {
                for (Registration i : registrations) {
                    if (nomUtilisateur.equalsIgnoreCase(i.getPlayer().getEmail())) {
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
                joinMatch(match, car);
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
        List<Car> cars = (List<Car>) Executions.getCurrent().getSession().getAttribute(VOITURES);

        carsListModel = new ListModelArray<>(cars);

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

    public ListModel<Car> getCarsListModel() {
        return carsListModel;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    private void enableCarProposal() {
        carProposalEnabled = true;

        try {
            List<Car> cars = carsManagementUIService.findCarsByPlayer();

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

    private void joinMatch(Match match, Car car) {
        try {
            matchsJoiningUIService.registerPlayer(match, car);

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
                    joinMatch(match, null);
                    break;
                default:
                    disableCarProposal();
                    break;
            }
        }
    }
}
