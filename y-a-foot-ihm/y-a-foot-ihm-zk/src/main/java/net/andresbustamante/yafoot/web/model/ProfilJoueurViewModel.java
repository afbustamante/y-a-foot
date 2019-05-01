package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.services.GestionProfilJoueurUIService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class ProfilJoueurViewModel extends AbstractViewModel {

    private Joueur joueur;
    private String ancienMotDePasse;
    private String nouveauMotDePasse;
    private String confirmationMotDePasse;

    @WireVariable
    private GestionProfilJoueurUIService gestionProfilJoueurUIService;

    @Init
    @Override
    public void init() {
        joueur = gestionProfilJoueurUIService.chargerProfilJoueurActuel();
    }

    @Command
    public void sauvegarderModifications() {
    }

    @Command
    public void desactiverCompte() {
    }

    @Command
    public void modifierMotDePasse() {
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setAncienMotDePasse(String ancienMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
    }

    public void setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    public void setConfirmationMotDePasse(String confirmationMotDePasse) {
        this.confirmationMotDePasse = confirmationMotDePasse;
    }
}
