package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.model.xs.Site;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author andresbustamante
 */
@VariableResolver(DelegatingVariableResolver.class)
public class AjoutSiteController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Window winNouveauSite;
    @Wire
    private Textbox tbxNomSite;
    @Wire
    private Textbox tbxAdresseSite;
    @Wire
    private Textbox tbxTelephoneSite;
    @Wire
    private transient ListModel<Site> sitesModel;

    @Listen("onClick = #btnContinue")
    public void ajouterSite() {
        fermerDialog();
    }

    @Listen("onClick = #btnCancel")
    public void fermerDialog() {
        winNouveauSite.detach();
    }
}
