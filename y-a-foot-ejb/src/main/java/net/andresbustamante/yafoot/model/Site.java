package net.andresbustamante.yafoot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author andresbustamante
 */
@Entity
@Table(name = "t_site")
@SequenceGenerator(name = "s_site", sequenceName = "s_site", allocationSize = 1)
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sit_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_site")
    private Integer id;
    @Size(max = 255)
    @Column(name = "sit_nom")
    private String nom;
    @Size(max = 255)
    @Column(name = "sit_adresse")
    private String adresse;
    @Size(max = 16)
    @Column(name = "sit_telephone")
    private String telephone;

    public Site() {
    }

    public Site(Integer id) {
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Site[ sitId=" + id + " ]";
    }
    
}
