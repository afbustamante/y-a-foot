package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import org.springframework.stereotype.Service;

@Service
public class InscriptionJoueursUIService extends AbstractUIService {

    /**
     * Créer un nouveau joueur dans l'application
     *
     * @param joueur Joueur à créer
     * @throws ApplicationException
     */
    public boolean creerNouveauCompteJoueur(Joueur joueur) throws ApplicationException {
        // TODO Implémenter cette méthode
        return true;
    }
}
