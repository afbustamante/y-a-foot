package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.model.xs.Site;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.*;

import java.util.ArrayList;

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
}
