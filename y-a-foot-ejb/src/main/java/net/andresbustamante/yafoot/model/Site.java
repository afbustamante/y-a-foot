package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andresbustamante
 */
@Entity
@Table(name = "t_site")
@NamedQueries({
    @NamedQuery(name = "Site.findAll", query = "SELECT s FROM Site s")})
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sit_id")
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
    private List<Match> matchs;

    public Site() {
    }

    public Site(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer sitId) {
        this.id = sitId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String sitNom) {
        this.nom = sitNom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String sitAdresse) {
        this.adresse = sitAdresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String sitTelephone) {
        this.telephone = sitTelephone;
    }

    public List<Match> getMatchs() {
        return matchs;
    }

    public void setMatchs(List<Match> matchList) {
        this.matchs = matchList;
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
