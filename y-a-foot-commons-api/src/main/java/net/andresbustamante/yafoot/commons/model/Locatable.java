package net.andresbustamante.yafoot.commons.model;

/**
 * Interface to be implemented by any object having GPS coordinates
 */
@FunctionalInterface
public interface Locatable {
    
    GpsCoordinates getLocation();
}
