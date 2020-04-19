package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Identifiable;

import java.io.Serializable;
import java.util.List;

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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Car[ carId=" + id + " ]";
    }

}
