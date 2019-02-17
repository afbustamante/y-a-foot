package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.services.InscriptionJoueursUIService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class InscriptionJoueurController extends SelectorComposer<Component> {

    private final Log log = LogFactory.getLog(InscriptionJoueursUIService.class);

    @Wire
    private Textbox txtFirstName;

    @Wire
    private Textbox txtSurname;

    @Wire
    private Textbox txtPassword1;

    @Wire
    private Textbox txtPassword2;

    @Wire
    private Textbox txtEmail;

    @Autowired
    private InscriptionJoueursUIService inscriptionJoueursUIService;

    @Listen("onClick = #btnContinue")
    public void enregistrerUtilisateur() {
        String prenom = txtFirstName.getValue();
        String nom = txtSurname.getValue();
        String motDePasse = txtPassword1.getValue();
        String email = txtEmail.getValue();

        Joueur joueur = new Joueur();
        joueur.setEmail(email);
        joueur.setNom(nom);
        joueur.setPrenom(prenom);
        joueur.setMotDePasse(motDePasse);

        try {
            boolean inscrit = inscriptionJoueursUIService.creerNouveauCompteJoueur(joueur);

            // TODO Afficher message à l'utilisateur
            // TODO Redirection vers la page d'accueil
        } catch (ApplicationException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            // TODO Afficher message à l'utilisateur
        }
    }
}
