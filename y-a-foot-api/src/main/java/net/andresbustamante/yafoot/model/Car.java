package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author andresbustamante
 */
public class Car implements Serializable, Identifiable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Integer numSeats;
    private Player driver;
    private List<Player> passengers;

    public Car() {
    }

    public Car(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(Integer numSeats) {
        this.numSeats = numSeats;
    }

    public Player getDriver() {
        return driver;
    }

    public void setDriver(Player driver) {
        this.driver = driver;
    }

    public List<Player> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Player> passengers) {
        this.passengers = passengers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car that = (Car) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Car[ id=" + id + " ]";
    }

}
