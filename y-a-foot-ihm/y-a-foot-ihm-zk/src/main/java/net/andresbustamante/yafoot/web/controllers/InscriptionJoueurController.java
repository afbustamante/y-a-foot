package net.andresbustamante.yafoot.web.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class InscriptionJoueurController extends SelectorComposer<Component> {

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

    @Listen("onClick = #btnContinue")
    public void enregistrerUtilisateur() {
        // TODO Implémenter méthode
    }
}
