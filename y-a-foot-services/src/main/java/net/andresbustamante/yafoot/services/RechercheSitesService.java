package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;

import javax.ejb.Local;
import java.util.List;

/**
 * @author andresbustamante
 */
@Local
public interface RechercheSitesService {

    /**
     *
     * @param idJoueur
     * @param contexte
     * @return
     * @throws BDDException
     */
    List<Site> chercherSitesParJoueur(Integer idJoueur, Contexte contexte) throws BDDException;
}
