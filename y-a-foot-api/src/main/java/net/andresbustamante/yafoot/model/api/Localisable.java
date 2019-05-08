package net.andresbustamante.yafoot.model.api;

import net.andresbustamante.yafoot.model.CoordonneesGPS;

public interface Localisable {
    
    CoordonneesGPS getLocalisation();

    void setLocalisation(CoordonneesGPS coordonneesGPS);
}
