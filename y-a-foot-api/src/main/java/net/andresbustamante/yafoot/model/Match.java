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
    private ZonedDateTime date;
    private String code;
    private String description;
    private Integer numPlayersMin;
    private Integer numPlayersMax;
    private Integer numRegisteredPlayers;
    private List<Inscription> registrations;
    private Site site;
    private Player creator;
    private boolean carpoolingEnabled;
    private boolean codeSharingEnabled;

    public Match() {
    }

    public Match(Integer id) {
        this.id = id;
    }

    public Match(Integer id, ZonedDateTime date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer matId) {
        this.id = matId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
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

    public Integer getNumPlayersMin() {
        return numPlayersMin;
    }

    public void setNumPlayersMin(Integer matNumJoueursMin) {
        this.numPlayersMin = matNumJoueursMin;
    }

    public Integer getNumPlayersMax() {
        return numPlayersMax;
    }

    public void setNumPlayersMax(Integer matNumJoueursMax) {
        this.numPlayersMax = matNumJoueursMax;
    }

    public Integer getNumRegisteredPlayers() {
        return numRegisteredPlayers;
    }

    public void setNumRegisteredPlayers(Integer numRegisteredPlayers) {
        this.numRegisteredPlayers = numRegisteredPlayers;
    }

    public List<Inscription> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Inscription> inscription) {
        this.registrations = inscription;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    public boolean isCarpoolingEnabled() {
        return carpoolingEnabled;
    }

    public void setCarpoolingEnabled(boolean carpoolingEnabled) {
        this.carpoolingEnabled = carpoolingEnabled;
    }

    public boolean isCodeSharingEnabled() {
        return codeSharingEnabled;
    }

    public void setCodeSharingEnabled(boolean codeSharingEnabled) {
        this.codeSharingEnabled = codeSharingEnabled;
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
