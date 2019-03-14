package net.andresbustamante.yafoot.web.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.Locale;

/**
 * @author andresbustamante
 */
@VariableResolver(DelegatingVariableResolver.class)
public class AbstractController extends SelectorComposer<Component> {

    protected static final String NL = "\n";

    private static final long serialVersionUID = 1L;
    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static final String SEPARATEUR_ACCEPT_LANGUAGE = ",";

    protected Locale getLocaleUtilisateur() {
        String[] languesAcceptees = Executions.getCurrent().getHeader(ACCEPT_LANGUAGE).split(SEPARATEUR_ACCEPT_LANGUAGE);
        return Locale.forLanguageTag(languesAcceptees[0]);
    }
}
