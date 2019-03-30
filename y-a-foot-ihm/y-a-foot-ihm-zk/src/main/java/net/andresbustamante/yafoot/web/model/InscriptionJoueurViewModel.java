package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.services.InscriptionJoueursUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

public class InscriptionJoueurViewModel extends AbstractViewModel {

    private String prenom;
    private String nom;
    private String motDePasse;
    private String email;
    private boolean inscrit;

    private final transient Logger log = LoggerFactory.getLogger(InscriptionJoueursUIService.class);

    @WireVariable
    private transient InscriptionJoueursUIService inscriptionJoueursUIService;

    @Init
    public void init() {
        inscrit = false;
    }

    @Command
    public void enregistrerUtilisateur() {
        Joueur joueur = new Joueur();
        joueur.setEmail(email);
        joueur.setNom(nom);
        joueur.setPrenom(prenom);
        joueur.setMotDePasse(motDePasse);

        try {
            inscrit = inscriptionJoueursUIService.creerNouveauCompteJoueur(joueur);
        } catch (ApplicationException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            Clients.showNotification(Labels.getLabel("application.exception.text", e.getMessage()), true);
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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
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
