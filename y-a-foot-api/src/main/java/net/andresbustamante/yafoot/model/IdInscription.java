package net.andresbustamante.yafoot.model;

import java.io.Serializable;

/**
 * @author andresbustamante
 */
public class IdInscription implements Serializable {

    private Integer idMatch;
    private Integer idJoueur;

    public IdInscription() {
    }

    public IdInscription(Integer idMatch, Integer idJoueur) {
        this.idMatch = idMatch;
        this.idJoueur = idJoueur;
    }

    public Integer getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Integer idMatch) {
        this.idMatch = idMatch;
    }

    public Integer getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(Integer idJoueur) {
        this.idJoueur = idJoueur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += idMatch;
        hash += idJoueur;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IdInscription)) {
            return false;
        }
        IdInscription other = (IdInscription) object;
        if (!this.idMatch.equals(other.idMatch)) {
            return false;
        }
        if (!this.idJoueur.equals(other.idJoueur)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IdInscription [ idMatch=" + idMatch + ", idJoueur=" + idJoueur + " ]";
    }

}
