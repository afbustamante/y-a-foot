package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author andresbustamante
 */
public class Joueur extends Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private String telephone;
    private List<Voiture> voitures;

    public Joueur() {
    }

    public Joueur(Integer id) {
        this.id = id;
    }

    public Joueur(Integer id, String nom, String prenom, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String jouNom) {
        this.nom = jouNom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String jouPrenom) {
        this.prenom = jouPrenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String jouTelephone) {
        this.telephone = jouTelephone;
    }

    public List<Voiture> getVoitures() {
        return voitures;
    }

    public void setVoitures(List<Voiture> voitureList) {
        this.voitures = voitureList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Joueur)) {
            return false;
        }
        Joueur other = (Joueur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Joueur[ jouId=" + id + " ]";
    }

}
