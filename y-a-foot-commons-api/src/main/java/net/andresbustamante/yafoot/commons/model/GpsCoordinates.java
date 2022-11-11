package net.andresbustamante.yafoot.commons.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * GPS locatable object.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class GpsCoordinates implements Serializable {

    /**
     * GPS latitude.
     */
    private BigDecimal latitude;

    /**
     * GPS longitude.
     */
    private BigDecimal longitude;

    /**
     * Constructor with both latitude and longitude values.
     *
     * @param latitude GPS latitude
     * @param longitude GPS longitude
     */
    public GpsCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
