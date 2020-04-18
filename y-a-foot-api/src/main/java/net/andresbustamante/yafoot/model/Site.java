package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.model.api.Locatable;

import java.io.Serializable;

/**
 *
 * @author andresbustamante
 */
public class Site implements Locatable, Serializable {

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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Site[ sitId=" + id + " ]";
    }
    
}
