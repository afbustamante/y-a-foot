package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author andresbustamante
 */
public class Joueur extends Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereMaj;
    private List<Voiture> voitures;

    public Joueur() {
    }

    public Joueur(Integer id) {
        this.id = id;
    }

    public Joueur(Integer id, String nom, String prenom, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateDerniereMaj() {
        return dateDerniereMaj;
    }

    public void setDateDerniereMaj(LocalDateTime dateDerniereMaj) {
        this.dateDerniereMaj = dateDerniereMaj;
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
