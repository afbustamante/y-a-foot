package net.andresbustamante.yafoot.commons.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class GpsCoordinates implements Serializable {

    private BigDecimal latitude;
    private BigDecimal longitude;

    public GpsCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
