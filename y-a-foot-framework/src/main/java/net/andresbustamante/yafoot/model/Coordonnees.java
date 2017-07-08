package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author andresbustamante
 */
public class Coordonnees implements Serializable {

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Coordonnees() {
    }

    public Coordonnees(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
