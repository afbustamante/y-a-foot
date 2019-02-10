package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.util.MessagesProperties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.security.Principal;
import java.util.Locale;

/**
 * FacesBean de base
 */
public abstract class AbstractFacesBean {

    protected String getNomUtilisateurActif() {
        Principal p = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        return (p != null) ? p.getName() : null;
    }

    protected void ajouterMessage(String resume, String detailMessage, FacesMessage.Severity severity, String composant) {
        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        String summary = MessagesProperties.getValue(resume, locale);
        String detail = MessagesProperties.getValue(detailMessage, locale);
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(composant, message);
    }

    protected void ajouterMessageInfo(String resume, String detailMessage, String composant) {
        ajouterMessage(resume, detailMessage, FacesMessage.SEVERITY_INFO, composant);
    }

    protected void ajouterMessageErreur(String resume, String detailMessage, String composant) {
        ajouterMessage(resume, detailMessage, FacesMessage.SEVERITY_ERROR, composant);
    }
}
