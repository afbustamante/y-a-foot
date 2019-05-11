package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.web.util.WebConstants;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author andresbustamante
 */
@VariableResolver(DelegatingVariableResolver.class)
public abstract class AbstractViewModel {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static final String SEPARATEUR_ACCEPT_LANGUAGE = ",";

    protected String getNomUtilisateurActif() {
        Session session = Sessions.getCurrent();
        SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    protected Locale getLocaleUtilisateur() {
        String[] languesAcceptees = Executions.getCurrent().getHeader(ACCEPT_LANGUAGE).split(SEPARATEUR_ACCEPT_LANGUAGE);
        return Locale.forLanguageTag(languesAcceptees[0]);
    }

    public DateFormat getDateFormat() {
        Session session = Sessions.getCurrent();
        Object obj = session.getAttribute(WebConstants.DATE_FORMAT);

        if (obj != null) {
            return (DateFormat) obj;
        } else {
            DateFormat dateFormat = new SimpleDateFormat(DateUtils.getPatternDateHeure(getLocaleUtilisateur().getLanguage()),
                    getLocaleUtilisateur());
            session.setAttribute(WebConstants.DATE_FORMAT, dateFormat);
            return dateFormat;
        }
    }

    public abstract void init();
}
