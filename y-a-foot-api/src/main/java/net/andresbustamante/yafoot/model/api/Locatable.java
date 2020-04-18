package net.andresbustamante.yafoot.model.api;

import net.andresbustamante.yafoot.model.GpsCoordinates;

@FunctionalInterface
public interface Locatable {
    
    GpsCoordinates getLocation();
}
