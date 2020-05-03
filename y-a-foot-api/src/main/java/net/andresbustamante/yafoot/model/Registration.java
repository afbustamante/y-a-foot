package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author andresbustamante
 */
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;
    private RegistrationId id;
    private Car car;
    private Player player;
    private boolean carConfirmed;

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
