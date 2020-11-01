package net.andresbustamante.yafoot.model.api;

import net.andresbustamante.yafoot.model.GpsCoordinates;

/**
 * Interface to be implemented by any object having GPS coordinates
 */
@FunctionalInterface
public interface Locatable {
    
    GpsCoordinates getLocation();
}
