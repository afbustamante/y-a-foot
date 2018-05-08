package net.andresbustamante.yafoot.model;

import java.io.Serializable;

/**
 * @author andresbustamante
 */
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;
    private IdInscription id;
    private Voiture voiture;
    private Match match;
    private Joueur joueur;

    public Inscription() {
    }

    public Inscription(IdInscription id) {
        this.id = id;
    }

    public Inscription(int idMatch, int idJoueur) {
        this.id = new IdInscription(idMatch, idJoueur);
    }

    public IdInscription getId() {
        return id;
    }

    public void setId(IdInscription id) {
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
        if (!(object instanceof Inscription)) {
            return false;
        }
        Inscription other = (Inscription) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this
                .id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Inscription[ id=" + id + " ]";
    }

}
