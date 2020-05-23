package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

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
    private List<Registration> registrations;
    private Site site;
    private Player creator;
    private boolean carpoolingEnabled;
    private boolean codeSharingEnabled;
    private ZonedDateTime creationDate;

    public Match() {
    }

    public Match(Integer id) {
        this.id = id;
    }

    public Match(Integer id, ZonedDateTime date) {
        this.id = id;
        this.date = date;
    }

    public boolean isPlayerRegistered(Player player) throws ApplicationException {
        if (registrations != null) {
            for (Registration registration : registrations) {
                if (registration.getPlayer().equals(player)) {
                    return true;
                }
            }
            return false;
        }
        throw new ApplicationException("No registrations information");
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
        if (registrations != null) {
            return registrations.size();
        }
        return 0;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registration) {
        this.registrations = registration;
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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match that = (Match) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return "Match [ id=" + id + " ]";
    }

}
