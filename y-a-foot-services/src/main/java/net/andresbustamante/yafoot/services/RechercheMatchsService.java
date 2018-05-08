package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;

import javax.ejb.Local;
import java.util.List;

/**
 * @author andresbustamante
 */
@Local
public interface RechercheMatchsService {

    /**
     *
     * @param codeMatch
     * @param contexte
     * @return
     * @throws BDDException
     */
    Match chercherMatchParCode(String codeMatch, Contexte contexte) throws BDDException;

    /**
     *
     * @param idJoueur
     * @param contexte
     * @return
     * @throws BDDException
     */
    List<Match> chercherMatchsJoueur(Integer idJoueur, Contexte contexte) throws BDDException;
}
