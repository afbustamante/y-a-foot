package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.services.InscriptionJoueursUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import static net.andresbustamante.yafoot.web.ConstantesWeb.PAGE_ACCUEIL;

public class InscriptionJoueurController extends AbstractController {

    private final transient Logger log = LoggerFactory.getLogger(InscriptionJoueursUIService.class);

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

    @WireVariable
    private transient InscriptionJoueursUIService inscriptionJoueursUIService;

    @Listen(Events.ON_CLICK + " = #btnContinue")
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

            if (inscrit) {
                EventListener<Messagebox.ClickEvent> clickListener = event -> Executions.sendRedirect(PAGE_ACCUEIL);
                Messagebox.show(Labels.getLabel("sign.in.successful"), Labels.getLabel("dialog.information.title"),
                        new Messagebox.Button[]{Messagebox.Button.OK}, Messagebox.INFORMATION, clickListener);
            } else {
                Clients.showNotification(Labels.getLabel("sign.in.error.existing.player"), txtEmail);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            Clients.showNotification(Labels.getLabel("application.exception.text", e.getMessage()), true);
        }
    }

    @Override
    protected void init() {
    }
}
