package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.web.services.PlayersProfileManagementUIService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

public class PlayerProfileViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(PlayerProfileViewModel.class);
    private Player player;
    private String newPassword;
    private String confirmationPassword;

    @WireVariable
    private PlayersProfileManagementUIService playersProfileManagementUIService;

    @Init
    @Override
    public void init() {
        try {
            player = playersProfileManagementUIService.loadProfileForPlayer();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des données pour un utilisateur", e);
        }
    }

    @Command
    public void updateProfile() {
        try {
            boolean succes = playersProfileManagementUIService.updateProfile(player);

            if (succes) {
                Messagebox.show(Labels.getLabel("player.profile.update.success"),
                        Labels.getLabel(DIALOG_INFORMATION_TITLE),
                        new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                        event -> Executions.getCurrent().sendRedirect(HOME_PAGE));
            } else {
                Messagebox.show(Labels.getLabel("player.profile.update.error"),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        Messagebox.Button.OK.id, Messagebox.ERROR);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la sauvegarde des données pour un utilisateur", e);
            Messagebox.show(Labels.getLabel(APPLICATION_EXCEPTION_TEXT, new String[]{e.getMessage()}),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    @Command
    public void deactivateProfile() {
        EventListener<Messagebox.ClickEvent> clickListener = event -> {
            if (Messagebox.Button.YES.equals(event.getButton())) {
                try {
                    boolean succes = playersProfileManagementUIService.deactivatePlayer(player);

                    if (succes) {
                        Messagebox.show(Labels.getLabel("player.profile.inactivate.success"),
                                Labels.getLabel(DIALOG_INFORMATION_TITLE),
                                new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                                sousEvent -> Executions.getCurrent().sendRedirect("/logout"));
                    } else {
                        Messagebox.show(Labels.getLabel("player.profile.inactivate.error"),
                                Labels.getLabel(DIALOG_ERROR_TITLE),
                                Messagebox.Button.OK.id, Messagebox.ERROR);
                    }
                } catch (ApplicationException e) {
                    log.error("Erreur lors de la désactivation du profil d'un utilisateur", e);
                    Messagebox.show(Labels.getLabel(APPLICATION_EXCEPTION_TEXT, new String[]{e.getMessage()}),
                            Labels.getLabel(DIALOG_ERROR_TITLE),
                            Messagebox.Button.OK.id, Messagebox.ERROR);
                }
            }
        };
        Messagebox.show(Labels.getLabel("player.profile.inactivate.confirmation.dialog.text"),
                Labels.getLabel(DIALOG_CONFIRMATION_TITLE),
                new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO},
                Messagebox.QUESTION, clickListener);
    }

    @Command
    public void updatePassword() {
        try {
            if (StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(confirmationPassword)) {
                if (newPassword.equals(confirmationPassword)) {
                    boolean succes = playersProfileManagementUIService.updatePlayerPassword(player.getEmail(), newPassword);

                    if (succes) {
                        Messagebox.show(Labels.getLabel("player.profile.update.success"),
                                Labels.getLabel(DIALOG_INFORMATION_TITLE),
                                new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                                event -> Executions.getCurrent().sendRedirect(HOME_PAGE));
                    } else {
                        Messagebox.show(Labels.getLabel("player.profile.error.invalid.passwd"),
                                Labels.getLabel(DIALOG_ERROR_TITLE),
                                Messagebox.Button.OK.id, Messagebox.ERROR);
                    }
                } else {
                    Messagebox.show(Labels.getLabel("player.profile.error.non.matching.passwd"),
                            Labels.getLabel(DIALOG_ERROR_TITLE),
                            Messagebox.Button.OK.id, Messagebox.ERROR);
                }
            } else {
                Messagebox.show(Labels.getLabel("player.profile.error.invalid.passwd"),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        Messagebox.Button.OK.id, Messagebox.ERROR);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la mise à jour du mot de passe pour un utilisateur", e);
            Messagebox.show(Labels.getLabel(APPLICATION_EXCEPTION_TEXT, new String[]{e.getMessage()}),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }
}
