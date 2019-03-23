package net.andresbustamante.yafoot.model;

import java.time.ZoneId;

/**
 * @author andresbustamante
 */
public class Contexte {

    public static final String UTILISATEUR = "ctxUser";
    public static final String TIMEZONE = "tz";

    private Integer idUtilisateur;
    private String emailUtilisateur;
    private ZoneId timeZone;

    public Contexte() {}

    public Contexte(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
