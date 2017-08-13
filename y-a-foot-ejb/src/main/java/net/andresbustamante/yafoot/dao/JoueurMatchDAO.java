package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.model.JoueurMatch;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface JoueurMatchDAO extends DAO<JoueurMatch> {
}
