package net.andresbustamante.yafoot.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author andresbustamante
 */
@Entity
@Table(name = "t_joueur_match")
public class JoueurMatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private IdJoueurMatch id;
    @JoinColumn(name = "jma_voiture_fk", referencedColumnName = "voi_id")
    @ManyToOne
    private Voiture voiture;
    @JoinColumn(name = "jma_match_fk", referencedColumnName = "mat_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Match match;
    @JoinColumn(name = "jma_joueur_fk", referencedColumnName = "jou_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Joueur joueur;

    public JoueurMatch() {
    }

    public JoueurMatch(IdJoueurMatch id) {
        this.id = id;
    }

    public JoueurMatch(int idMatch, int idJoueur) {
        this.id = new IdJoueurMatch(idMatch, idJoueur);
    }

    public IdJoueurMatch getId() {
        return id;
    }

    public void setId(IdJoueurMatch id) {
        this.id = id;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof JoueurMatch)) {
            return false;
        }
        JoueurMatch other = (JoueurMatch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this
                .id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.JoueurMatch[ joueurMatchPK=" + id + " ]";
    }

}
