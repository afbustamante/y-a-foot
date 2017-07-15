package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andresbustamante
 */
@Entity
@Table(name = "t_voiture")
@NamedQueries({
    @NamedQuery(name = "Voiture.findAll", query = "SELECT v FROM Voiture v")})
@SequenceGenerator(name = "s_voiture", sequenceName = "s_voiture", allocationSize = 1)
public class Voiture implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "voi_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_voiture")
    private Integer id;
    @Size(max = 255)
    @Column(name = "voi_nom")
    private String nom;
    @Column(name = "voi_num_places")
    private Integer numPlaces;
    @OneToMany(mappedBy = "voiture")
    private List<JoueurMatch> passagers;
    @JoinColumn(name = "voi_chauffeur_fk", referencedColumnName = "jou_id")
    @ManyToOne(optional = false)
    private Joueur chauffeur;

    public Voiture() {
    }

    public Voiture(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer voiId) {
        this.id = voiId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String voiNom) {
        this.nom = voiNom;
    }

    public Integer getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(Integer voiNumPlaces) {
        this.numPlaces = voiNumPlaces;
    }

    public List<JoueurMatch> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<JoueurMatch> joueurMatchList) {
        this.passagers = joueurMatchList;
    }

    public Joueur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Joueur voiChauffeurFk) {
        this.chauffeur = voiChauffeurFk;
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
