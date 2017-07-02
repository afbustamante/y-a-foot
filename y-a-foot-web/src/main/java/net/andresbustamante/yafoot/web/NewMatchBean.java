package net.andresbustamante.yafoot.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.math.BigDecimal;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class NewMatchBean {

    private String dateMatch;
    private String heureMatch;
    private Integer numMinJoueurs;
    private Integer numMaxJoueurs;
    private String nomSite;
    private BigDecimal latitudeSite;
    private BigDecimal longitudeSite;

    public NewMatchBean() {
    }

    public String getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(String dateMatch) {
        this.dateMatch = dateMatch;
    }

    public String getHeureMatch() {
        return heureMatch;
    }

    public void setHeureMatch(String heureMatch) {
        this.heureMatch = heureMatch;
    }

    public Integer getNumMinJoueurs() {
        return numMinJoueurs;
    }

    public void setNumMinJoueurs(Integer numMinJoueurs) {
        this.numMinJoueurs = numMinJoueurs;
    }

    public Integer getNumMaxJoueurs() {
        return numMaxJoueurs;
    }

    public void setNumMaxJoueurs(Integer numMaxJoueurs) {
        this.numMaxJoueurs = numMaxJoueurs;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public BigDecimal getLatitudeSite() {
        return latitudeSite;
    }

    public void setLatitudeSite(BigDecimal latitudeSite) {
        this.latitudeSite = latitudeSite;
    }

    public BigDecimal getLongitudeSite() {
        return longitudeSite;
    }

    public void setLongitudeSite(BigDecimal longitudeSite) {
        this.longitudeSite = longitudeSite;
    }
}
