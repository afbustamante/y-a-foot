package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Identifiable;
import net.andresbustamante.yafoot.model.api.Locatable;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author andresbustamante
 */
public class Site implements Locatable, Identifiable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private GpsCoordinates location;

    public Site() {
    }

    public Site(Integer id) {
        this.id = id;
    }

    public Site(String name, String address, String phoneNumber, GpsCoordinates location) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.location = location;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public GpsCoordinates getLocation() {
        return location;
    }

    public void setLocation(GpsCoordinates location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site that = (Site) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Site[ id=" + id + " ]";
    }
    
}
