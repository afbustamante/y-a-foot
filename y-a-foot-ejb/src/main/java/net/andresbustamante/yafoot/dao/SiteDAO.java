package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Site;

import javax.ejb.Local;
import java.util.List;

/**
 * @author andresbustamante
 */
@Local
public interface SiteDAO extends DAO<Site> {

    /**
     * Chercher des sites à partir d'un nom
     *
     * @param nom Nom du site
     * @return Liste de sites contenant le nom passé en paramètre
     */
    List<Site> chercherParNom(String nom) throws BDDException;
}
