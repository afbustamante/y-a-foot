package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Locatable;

import java.io.Serializable;

/**
 *
 * @author andresbustamante
 */
public class Site implements Locatable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nom;
    private String adresse;
    private String telephone;
    private CoordonneesGPS localisation;

    public Site() {
    }

    public Site(Integer id) {
        this.id = id;
    }

    public Site(String nom, String adresse, String telephone, CoordonneesGPS localisation) {
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.localisation = localisation;
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
    public CoordonneesGPS getLocalisation() {
        return localisation;
    }

    @Override
    public void setLocalisation(CoordonneesGPS localisation) {
        this.localisation = localisation;
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
