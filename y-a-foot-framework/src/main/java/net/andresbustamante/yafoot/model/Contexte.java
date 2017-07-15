package net.andresbustamante.yafoot.model;

/**
 * @author andresbustamante
 */
public class Contexte {

    private Integer idUtilisateur;
    private String emailUtilisateur;
    private String[] rolesUtilisateur;

    public Contexte() {}

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

    public String[] getRolesUtilisateur() {
        return rolesUtilisateur;
    }

    public void setRolesUtilisateur(String[] rolesUtilisateur) {
        this.rolesUtilisateur = rolesUtilisateur;
    }
}
