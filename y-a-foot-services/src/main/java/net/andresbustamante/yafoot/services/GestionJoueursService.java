package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;

/**
 * Service de gestion des joueurs
 *
 * @author andresbustamante
 */
public interface GestionJoueursService {

    /**
     * Inscrire un joueur dans le système
     *
     * @param joueur Joueur à créer
     * @param contexte
     * @return
     * @throws BDDException
     */
    boolean inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException;

    /**
     * Mettre à jour les informations de base d'un joeur
     *
     * @param joueur Joueur à mettre à jour
     * @param contexte
     * @return
     * @throws BDDException
     */
    boolean actualiserJoueur(Joueur joueur, Contexte contexte) throws BDDException;
}
