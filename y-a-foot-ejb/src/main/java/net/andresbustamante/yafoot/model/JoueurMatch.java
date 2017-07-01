package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author andresbustamante
 */
@Entity
@Table(name = "t_joueur_match")
@NamedQueries({
        @NamedQuery(name = "JoueurMatch.findAll", query = "SELECT j FROM JoueurMatch j")})
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

    public JoueurMatch(int jmaMatchFk, int jmaJoueurFk) {
        this.id = new IdJoueurMatch(jmaMatchFk, jmaJoueurFk);
    }

    public IdJoueurMatch getId() {
        return id;
    }

    public void setId(IdJoueurMatch idJoueurMatch) {
        this.id = idJoueurMatch;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture jmaVoitureFk) {
        this.voiture = jmaVoitureFk;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
