package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;

/**
 * @author andresbustamante
 */
public interface GestionJoueursService {

    boolean inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException;

    boolean actualiserJoueur(Joueur joueur, Contexte contexte) throws BDDException;

    Joueur chercherJoueur(String email, Contexte contexte) throws BDDException;
}
