package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.api.Auditable;
import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author andresbustamante
 */
public class Match implements Serializable, Identifiable, Auditable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private OffsetDateTime date;
    private String code;
    private String description;
    private Integer numPlayersMin;
    private Integer numPlayersMax;
    private List<Registration> registrations;
    private Site site;
    private Player creator;
    private boolean carpoolingEnabled;
    private boolean codeSharingEnabled;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;

    public Match() {
    }

    public Match(Integer id) {
        this.id = id;
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

    /**
     * A match is accepting registrations if the date of the match is in the future and the number of players expected
     * is still higher than the number of players registered for the match
     *
     * @return
     */
    public boolean isAcceptingRegistrations() {
        if (OffsetDateTime.now().isBefore(date)) {
            return (numPlayersMax == null) || numPlayersMax > getNumRegisteredPlayers();
        } else {
            return false;
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer matId) {
        this.id = matId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
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

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public OffsetDateTime getLastUpdateDate() {
        return lastUpdateDate;
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
