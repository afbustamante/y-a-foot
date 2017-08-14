package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * @author andresbustamante
 */
@Embeddable
public class IdJoueurMatch implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "jma_match_fk")
    private Integer idMatch;
    @Basic(optional = false)
    @NotNull
    @Column(name = "jma_joueur_fk")
    private Integer idJoueur;

    public IdJoueurMatch() {
    }

    public IdJoueurMatch(Integer idMatch, Integer idJoueur) {
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
        if (!(object instanceof IdJoueurMatch)) {
            return false;
        }
        IdJoueurMatch other = (IdJoueurMatch) object;
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
        return "net.andresbustamante.yafoot.model.IdJoueurMatch[ jmaMatchFk=" + idMatch + ", jmaJoueurFk=" +
                idJoueur + " ]";
    }

}
