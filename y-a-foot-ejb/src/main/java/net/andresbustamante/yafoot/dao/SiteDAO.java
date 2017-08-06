package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.model.Site;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface SiteDAO extends DAO<Site> {
}
