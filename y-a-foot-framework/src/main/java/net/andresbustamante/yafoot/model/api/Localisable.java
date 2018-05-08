package net.andresbustamante.yafoot.model.api;

import net.andresbustamante.yafoot.model.Coordonnees;

public interface Localisable {
    
    Coordonnees getLocalisation();

    void setLocalisation(Coordonnees coordonnees);
}
