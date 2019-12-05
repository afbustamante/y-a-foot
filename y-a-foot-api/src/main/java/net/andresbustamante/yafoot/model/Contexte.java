package net.andresbustamante.yafoot.model;

import java.time.ZoneId;

/**
 * @author andresbustamante
 */
public class Contexte {

    public static final String UTILISATEUR = "userCtx";
    public static final String TZ = "tz";

    private Integer idUtilisateur;
    private String emailUtilisateur;
    private ZoneId timezone;

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

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }
}
