package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Joueur;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface JoueurDAO extends DAO<Joueur> {

    boolean isJoueurInscrit(String email) throws BDDException;
}
