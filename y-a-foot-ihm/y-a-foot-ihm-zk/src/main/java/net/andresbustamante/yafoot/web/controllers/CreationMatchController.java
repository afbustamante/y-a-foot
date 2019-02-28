package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.model.xs.Site;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author andresbustamante
 */
@VariableResolver(DelegatingVariableResolver.class)
public class CreationMatchController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Datebox txtDateMatch;

    @Wire
    private Spinner txtNbMinJoueurs;

    @Wire
    private Spinner txtNbMaxJoueurs;

    @Wire
    private Combobox cbbSite;

    private ListModel<Site> sitesModel = new ListModelArray<>(new ArrayList<>());

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        cbbSite.setModel(sitesModel);
    }

    @Listen("onClick = #btnNouveauSite")
    public void afficherDialogCreationSite() {
        Map<String, Object> arguments = new WeakHashMap<>();
        arguments.put("sitesModel", sitesModel);
        String template = "/sites/new_site_dialog.zul";
        Window window = (Window) Executions.createComponents(template, this.getSelf(), arguments);
        window.doModal();
    }
}
