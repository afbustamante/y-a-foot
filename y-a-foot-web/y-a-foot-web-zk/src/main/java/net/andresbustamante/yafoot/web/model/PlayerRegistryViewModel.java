package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Player;
import net.andresbustamante.yafoot.web.services.PlayersRegistryUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.nio.charset.StandardCharsets;

public class PlayerRegistryViewModel extends AbstractViewModel {

    private String firstName;
    private String surname;
    private String password1;
    private String password2;
    private String email;
    private boolean registered;

    private final Logger log = LoggerFactory.getLogger(PlayerRegistryViewModel.class);

    @WireVariable
    private PlayersRegistryUIService playersRegistryUIService;

    @Init
    public void init() {
        registered = false;
    }

    @Command
    public void saveUser() {
        if (password1 != null && password1.equals(password2)) {
            Player player = new Player();
            player.setEmail(email);
            player.setSurname(surname);
            player.setFirstName(firstName);
            player.setPassword(password1.getBytes(StandardCharsets.UTF_8));

            try {
                registered = playersRegistryUIService.savePlayer(player);

                if (registered) {
                    Messagebox.show(Labels.getLabel("sign.in.successful"),
                            Labels.getLabel(DIALOG_INFORMATION_TITLE),
                            Messagebox.Button.OK.id,
                            Messagebox.INFORMATION, event -> Executions.getCurrent().sendRedirect("/"));
                } else {
                    Messagebox.show(Labels.getLabel("sign.in.error.existing.player"),
                            Labels.getLabel(DIALOG_INFORMATION_TITLE),
                            Messagebox.Button.OK.id,
                            Messagebox.EXCLAMATION);
                }
            } catch (ApplicationException e) {
                log.error("Erreur lors de l'inscription d'un player", e);
                Messagebox.show(Labels.getLabel("application.exception.text", new String[]{e.getMessage()}),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        Messagebox.Button.OK.id, Messagebox.ERROR);
            }
        } else {
            Messagebox.show(Labels.getLabel("sign.in.password.confirmation.does.not.match"),
                    Labels.getLabel(DIALOG_INFORMATION_TITLE),
                    Messagebox.Button.OK.id,
                    Messagebox.EXCLAMATION);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRegistered() {
        return registered;
    }
}
