package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;

/**
 * Service de recherche de joueurs
 *
 * @author andresbustamante
 */
public interface RechercheJoueursService {

    /**
     * Chercher un joueur par son adresse mail unique
     *
     * @param email Adresse mail à chercher
     * @param contexte
     * @return
     * @throws BDDException
     */
    Joueur chercherJoueur(String email, Contexte contexte) throws BDDException;
}
