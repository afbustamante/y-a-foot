package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.services.InscriptionJoueursUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.nio.charset.StandardCharsets;

public class InscriptionJoueurViewModel extends AbstractViewModel {

    private String prenom;
    private String nom;
    private String motDePasse1;
    private String motDePasse2;
    private String email;
    private boolean inscrit;

    private final Logger log = LoggerFactory.getLogger(InscriptionJoueurViewModel.class);

    @WireVariable
    private InscriptionJoueursUIService inscriptionJoueursUIService;

    @Init
    public void init() {
        inscrit = false;
    }

    @Command
    public void enregistrerUtilisateur() {
        if (motDePasse1 != null && motDePasse1.equals(motDePasse2)) {
            Joueur joueur = new Joueur();
            joueur.setEmail(email);
            joueur.setNom(nom);
            joueur.setPrenom(prenom);
            joueur.setMotDePasse(motDePasse1.getBytes(StandardCharsets.UTF_8));

            try {
                inscrit = inscriptionJoueursUIService.creerNouveauCompteJoueur(joueur);

                if (inscrit) {
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
                log.error("Erreur lors de l'inscription d'un joueur", e);
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMotDePasse1() {
        return motDePasse1;
    }

    public void setMotDePasse1(String motDePasse1) {
        this.motDePasse1 = motDePasse1;
    }

    public String getMotDePasse2() {
        return motDePasse2;
    }

    public void setMotDePasse2(String motDePasse2) {
        this.motDePasse2 = motDePasse2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isInscrit() {
        return inscrit;
    }
}
