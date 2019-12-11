package net.andresbustamante.yafoot.model.api;

import net.andresbustamante.yafoot.model.CoordonneesGPS;

public interface Locatable {
    
    CoordonneesGPS getLocalisation();

    void setLocalisation(CoordonneesGPS coordonneesGPS);
}
