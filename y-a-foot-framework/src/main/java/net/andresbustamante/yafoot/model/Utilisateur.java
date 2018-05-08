package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Identifiable;

public abstract class Utilisateur implements Identifiable {

    protected Integer id;
    protected String email;
    protected String motDePasse;

    public Utilisateur() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
