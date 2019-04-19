package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;

/**
 * Service de gestion des matchs
 *
 * @author andresbustamante
 */
public interface GestionMatchsService {

    /**
     * Créer un match dans le système
     *
     * @param match Match à créer
     * @param contexte
     * @throws BDDException
     */
    boolean creerMatch(Match match, Contexte contexte) throws BDDException;

    /**
     * Inscrire un joueur à un match existant
     *
     * @param joueur Joueur à inscrire dans le match
     * @param match Match à impacter avec cette inscription
     * @param voiture Voiture dans laquelle le joueur va se déplacer vers le site de jeu
     * @param contexte
     * @throws BDDException
     */
    boolean inscrireJoueurMatch(Joueur joueur, Match match, Voiture voiture, Contexte contexte) throws BDDException;

    /**
     * Désinscrire un joueur d'un match
     *
     * @param joueur Joueur à désinscrire
     * @param match Match concerné
     * @param contexte
     * @return
     * @throws BDDException
     */
    boolean desinscrireJoueurMatch(Joueur joueur, Match match, Contexte contexte) throws BDDException;
}
