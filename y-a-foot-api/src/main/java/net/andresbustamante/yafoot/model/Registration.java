package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Auditable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Registration from a player to a match
 *
 * @author andresbustamante
 */
public class Registration implements Serializable, Auditable {

    private static final long serialVersionUID = 1L;

    /**
     * Registration ID between the match and the player
     */
    private RegistrationId id;

    /**
     * The car chosen for the registration
     */
    private Car car;

    /**
     * The player registered to the match
     */
    private Player player;

    /**
     * Indicates if the car chosen for the match has been confirmed by its owner
     */
    private boolean carConfirmed;

    /**
     * First date and time of the registration
     */
    private OffsetDateTime creationDate;

    /**
     * Date and time of the last update for the registration
     */
    private OffsetDateTime lastUpdateDate;

    public Registration() {}

    public Registration(RegistrationId id) {
        this.id = id;
    }

    public Registration(int matchId, int playerId) {
        this.id = new RegistrationId(matchId, playerId);
    }

    public RegistrationId getId() {
        return id;
    }

    public void setId(RegistrationId id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isCarConfirmed() {
        return carConfirmed;
    }

    public void setCarConfirmed(boolean carConfirmed) {
        this.carConfirmed = carConfirmed;
    }

    @Override
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
        Registration that = (Registration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Registration [ id=" + id + " ]";
    }

}
