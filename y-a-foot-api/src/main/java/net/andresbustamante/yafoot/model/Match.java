package net.andresbustamante.yafoot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.model.api.Auditable;
import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
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

    public Match(Integer id) {
        this.id = id;
    }

    public boolean isPlayerRegistered(Player player) {
        if (registrations != null) {
            for (Registration registration : registrations) {
                if (registration.getPlayer().equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A match is accepting registrations if the date of the match is in the future and the number of players expected
     * is still higher than the number of players registered for the match
     *
     * @return True if the match is still accepting registrations
     */
    public boolean isAcceptingRegistrations() {
        if (OffsetDateTime.now().isBefore(date)) {
            return (numPlayersMax == null) || numPlayersMax > getNumRegisteredPlayers();
        } else {
            return false;
        }
    }

    public Integer getNumRegisteredPlayers() {
        if (registrations != null) {
            return registrations.size();
        }
        return 0;
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
