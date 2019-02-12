package net.andresbustamante.yafoot.web.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class AuthentificationController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Textbox txtUsername;

    @Wire
    private Textbox txtPassword;

    @Listen("onClick = #btnValidate")
    public void authentifierUtilisateur() {
        // TODO Implémenter cette méthode
    }
}
