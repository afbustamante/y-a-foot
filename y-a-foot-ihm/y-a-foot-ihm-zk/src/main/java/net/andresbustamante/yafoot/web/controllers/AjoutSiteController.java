package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.model.xs.Site;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author andresbustamante
 */
public class AjoutSiteController extends AbstractController {

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

    @Listen(Events.ON_CLICK + " = #btnContinue")
    public void ajouterSite() {
        fermerDialog();
    }

    @Listen(Events.ON_CLICK + " = #btnCancel")
    public void fermerDialog() {
        winNouveauSite.detach();
    }
}
