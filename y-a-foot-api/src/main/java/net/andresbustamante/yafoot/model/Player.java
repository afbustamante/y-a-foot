package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Auditable;
import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author andresbustamante
 */
public class Player extends User implements Serializable, Identifiable, Auditable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String phoneNumber;
    private boolean active;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;
    private List<Car> cars;

    public Player() {}

    public Player(Integer id) {
        this.id = id;
    }

    public Player(Integer id, String surname, String firstName, String email, String phoneNumber) {
        this.id = id;
        this.surname = surname;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public OffsetDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> carList) {
        this.cars = carList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player that = (Player) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, email);
    }

    @Override
    public String toString() {
        return "Player[ id=" + id + " ]";
    }

}
