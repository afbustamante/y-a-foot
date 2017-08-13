package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.model.Voiture;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface VoitureDAO extends DAO<Voiture> {
}
