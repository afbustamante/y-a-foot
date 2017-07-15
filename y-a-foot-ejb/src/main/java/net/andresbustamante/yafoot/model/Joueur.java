package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author andresbustamante
 */
@Entity
@Table(name = "t_joueur")
@NamedQueries({
        @NamedQuery(name = "Joueur.findIdByEmail", query = "SELECT jou.id FROM Joueur jou WHERE jou.email = :email")})
@SequenceGenerator(name = "s_joueur", sequenceName = "s_joueur", allocationSize = 1)
public class Joueur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "jou_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_joueur")
    private Integer id;
    @Size(min = 1, max = 255)
    @Column(name = "jou_nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "jou_prenom")
    private String prenom;
    @Size(max = 16)
    @Column(name = "jou_telephone")
    private String telephone;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "jou_email")
    private String email;
    @Size(max = 64)
    @Column(name = "jou_mdp")
    private String motDePasse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "joueur")
    private List<JoueurMatch> matchs;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chauffeur")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer jouId) {
        this.id = jouId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String jouEmail) {
        this.email = jouEmail;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<JoueurMatch> getMatchs() {
        return matchs;
    }

    public void setMatchs(List<JoueurMatch> joueurMatchList) {
        this.matchs = joueurMatchList;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
