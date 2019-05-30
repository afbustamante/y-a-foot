package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;

import java.util.List;

/**
 * @author andresbustamante
 */
public interface RechercheSitesService {

    /**
     *
     * @param idJoueur
     * @param contexte
     * @return
     * @throws DatabaseException
     */
    List<Site> chercherSitesParJoueur(Integer idJoueur, Contexte contexte) throws DatabaseException;
}
