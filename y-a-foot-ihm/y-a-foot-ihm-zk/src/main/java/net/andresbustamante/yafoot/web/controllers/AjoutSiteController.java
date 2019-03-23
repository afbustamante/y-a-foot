package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.model.xs.Site;
import org.apache.commons.lang3.StringUtils;
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
        String nomSite = tbxNomSite.getValue();
        String adresseSite = tbxAdresseSite.getValue();
        String telephoneSite = tbxTelephoneSite.getValue();

        Site nouveauSite = buildSite(nomSite, adresseSite, telephoneSite);

        // TODO Injecter le site dans la liste des sites du composant parent
        //getSelf().getParent().getChildren();

        fermerDialog();
    }

    @Listen(Events.ON_CLICK + " = #btnCancel")
    public void fermerDialog() {
        winNouveauSite.detach();
    }

    private Site buildSite(String nomSite, String adresseSite, String telephoneSite) {
        Site nouveauSite = new Site();
        nouveauSite.setId(0);
        nouveauSite.setNom(nomSite);
        nouveauSite.setAdresse(adresseSite);
        nouveauSite.setNumeroTelephone(StringUtils.isNotBlank(telephoneSite) ? telephoneSite : null);
        return nouveauSite;
    }

    @Override
    protected void init() {
    }
}
