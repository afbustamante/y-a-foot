package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface GestionJoueursService {

    void inscrireJoueur(Joueur joueur, Contexte contexte) throws BDDException;

    void actualiserJoueur(Joueur joueur, Contexte contexte) throws BDDException;

    Joueur chercherJoueur(String email, Contexte contexte) throws BDDException;
}
