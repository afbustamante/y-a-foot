package net.andresbustamante.yafoot.web.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

/**
 * @author andresbustamante
 */
@VariableResolver(DelegatingVariableResolver.class)
public class CreationMatchController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;
}
