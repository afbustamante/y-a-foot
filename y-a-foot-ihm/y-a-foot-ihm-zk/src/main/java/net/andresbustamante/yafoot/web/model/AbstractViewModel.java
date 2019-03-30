package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.web.util.ConstantesWeb;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author andresbustamante
 */
@VariableResolver(DelegatingVariableResolver.class)
public abstract class AbstractViewModel {

    protected static final String NL = "\n";

    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static final String SEPARATEUR_ACCEPT_LANGUAGE = ",";

    protected Locale getLocaleUtilisateur() {
        String[] languesAcceptees = Executions.getCurrent().getHeader(ACCEPT_LANGUAGE).split(SEPARATEUR_ACCEPT_LANGUAGE);
        return Locale.forLanguageTag(languesAcceptees[0]);
    }

    public DateFormat getDateFormat() {
        Session session = Sessions.getCurrent();
        Object obj = session.getAttribute(ConstantesWeb.DATE_FORMAT);

        if (obj != null) {
            return (DateFormat) obj;
        } else {
            DateFormat dateFormat = new SimpleDateFormat(DateUtils.getPatternDateHeure(getLocaleUtilisateur().getLanguage()),
                    getLocaleUtilisateur());
            session.setAttribute(ConstantesWeb.DATE_FORMAT, dateFormat);
            return dateFormat;
        }
    }

    public abstract void init();
}