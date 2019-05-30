package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.services.GestionProfilJoueurUIService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

public class ProfilJoueurViewModel extends AbstractViewModel {

    private final Logger log = LoggerFactory.getLogger(ProfilJoueurViewModel.class);
    private Joueur joueur;
    private String nouveauMotDePasse;
    private String confirmationMotDePasse;

    @WireVariable
    private GestionProfilJoueurUIService gestionProfilJoueurUIService;

    @Init
    @Override
    public void init() {
        try {
            joueur = gestionProfilJoueurUIService.chargerProfilJoueurActuel();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des données pour un utilisateur", e);
        }
    }

    @Command
    public void sauvegarderModifications() {
        try {
            boolean succes = gestionProfilJoueurUIService.actualiserDonneesJoueur(joueur);

            if (succes) {
                Messagebox.show(Labels.getLabel("player.profile.update.success"),
                        Labels.getLabel(DIALOG_INFORMATION_TITLE),
                        new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                        event -> Executions.getCurrent().sendRedirect(ACCUEIL));
            } else {
                Messagebox.show(Labels.getLabel("player.profile.update.error"),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        Messagebox.Button.OK.id, Messagebox.ERROR);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la sauvegarde des données pour un utilisateur", e);
            Messagebox.show(Labels.getLabel("application.exception.text", new String[]{e.getMessage()}),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    @Command
    public void desactiverCompte() {
        try {
            boolean succes = gestionProfilJoueurUIService.desactiverJoueur(joueur);

            if (succes) {
                Messagebox.show(Labels.getLabel("player.profile.inactivate.success"),
                        Labels.getLabel(DIALOG_INFORMATION_TITLE),
                        new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                        event -> Executions.getCurrent().sendRedirect("/logout"));
            } else {
                Messagebox.show(Labels.getLabel("player.profile.inactivate.error"),
                        Labels.getLabel(DIALOG_ERROR_TITLE),
                        Messagebox.Button.OK.id, Messagebox.ERROR);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la désactivation du profil d'un utilisateur", e);
            Messagebox.show(Labels.getLabel("application.exception.text", new String[]{e.getMessage()}),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    @Command
    public void modifierMotDePasse() {
        try {
            if (StringUtils.isNotBlank(nouveauMotDePasse) && StringUtils.isNotBlank(confirmationMotDePasse)) {
                if (nouveauMotDePasse.equals(confirmationMotDePasse)) {
                    boolean succes = gestionProfilJoueurUIService.actualiserMotDePasseJoueur(joueur.getEmail(), nouveauMotDePasse);

                    if (succes) {
                        Messagebox.show(Labels.getLabel("player.profile.update.success"),
                                Labels.getLabel(DIALOG_INFORMATION_TITLE),
                                new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION,
                                event -> Executions.getCurrent().sendRedirect(ACCUEIL));
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
            Messagebox.show(Labels.getLabel("application.exception.text", new String[]{e.getMessage()}),
                    Labels.getLabel(DIALOG_ERROR_TITLE),
                    Messagebox.Button.OK.id, Messagebox.ERROR);
        }
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    public void setConfirmationMotDePasse(String confirmationMotDePasse) {
        this.confirmationMotDePasse = confirmationMotDePasse;
    }
}
