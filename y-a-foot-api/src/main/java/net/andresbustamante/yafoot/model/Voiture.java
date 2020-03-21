package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author andresbustamante
 */
public class Voiture implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nom;
    private Integer nbPlaces;
    private Player chauffeur;
    private List<Player> passagers;

    public Voiture() {
    }

    public Voiture(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public Player getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Player chauffeur) {
        this.chauffeur = chauffeur;
    }

    public List<Player> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<Player> passagers) {
        this.passagers = passagers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Voiture)) {
            return false;
        }
        Voiture other = (Voiture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Voiture[ voiId=" + id + " ]";
    }

}
