package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author andresbustamante
 */
public class Match implements Serializable, Identifiable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private ZonedDateTime dateMatch;
    private String code;
    private String description;
    private Integer nbJoueursMin;
    private Integer nbJoueursMax;
    private Integer nbJoueursInscrits;
    private List<Inscription> inscriptions;
    private Site site;
    private Player createur;
    private boolean covoiturageActif;
    private boolean partageActif;

    public Match() {
    }

    public Match(Integer id) {
        this.id = id;
    }

    public Match(Integer id, ZonedDateTime dateMatch) {
        this.id = id;
        this.dateMatch = dateMatch;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer matId) {
        this.id = matId;
    }

    public ZonedDateTime getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(ZonedDateTime date) {
        this.dateMatch = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String matDescription) {
        this.description = matDescription;
    }

    public Integer getNbJoueursMin() {
        return nbJoueursMin;
    }

    public void setNbJoueursMin(Integer matNumJoueursMin) {
        this.nbJoueursMin = matNumJoueursMin;
    }

    public Integer getNbJoueursMax() {
        return nbJoueursMax;
    }

    public void setNbJoueursMax(Integer matNumJoueursMax) {
        this.nbJoueursMax = matNumJoueursMax;
    }

    public Integer getNbJoueursInscrits() {
        return nbJoueursInscrits;
    }

    public void setNbJoueursInscrits(Integer nbJoueursInscrits) {
        this.nbJoueursInscrits = nbJoueursInscrits;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscription) {
        this.inscriptions = inscription;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Player getCreateur() {
        return createur;
    }

    public void setCreateur(Player createur) {
        this.createur = createur;
    }

    public boolean isCovoiturageActif() {
        return covoiturageActif;
    }

    public void setCovoiturageActif(boolean covoiturageActif) {
        this.covoiturageActif = covoiturageActif;
    }

    public boolean isPartageActif() {
        return partageActif;
    }

    public void setPartageActif(boolean partageActif) {
        this.partageActif = partageActif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Match)) {
            return false;
        }
        Match other = (Match) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Match [ id=" + id + " ]";
    }

}
