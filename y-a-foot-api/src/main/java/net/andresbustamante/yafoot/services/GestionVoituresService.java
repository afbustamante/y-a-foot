package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Voiture;

/**
 * Service for managing cars in the DB
 */
public interface GestionVoituresService {

    /**
     * Registers a new car in the database
     *
     * @param car New car to register
     * @param ctx User context
     * @return
     * @throws DatabaseException
     */
    int enregistrerVoiture(Voiture car, Contexte ctx) throws DatabaseException;
}
