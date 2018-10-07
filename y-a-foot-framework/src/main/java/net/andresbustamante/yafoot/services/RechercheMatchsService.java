package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;

import java.util.List;

/**
 * Service de recherche des matchs
 *
 * @author andresbustamante
 */
public interface RechercheMatchsService {

    /**
     * Chercher un match par son code unique
     *
     * @param codeMatch Code du match à chercher
     * @param contexte
     * @return
     * @throws BDDException
     */
    Match chercherMatchParCode(String codeMatch, Contexte contexte) throws BDDException;

    /**
     * Chercher les matchs à venir pour un joueur dont l'identifiant est passé en paramètre
     *
     * @param idJoueur Identifiant du joueur
     * @param contexte
     * @return
     * @throws BDDException
     */
    List<Match> chercherMatchsJoueur(Integer idJoueur, Contexte contexte) throws BDDException;
}
