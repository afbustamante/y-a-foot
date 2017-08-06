package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.model.Match;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface MatchDAO extends DAO<Match> {

    /**
     *
     * @param codeMatch
     * @return
     */
    boolean isCodeExistant(String codeMatch);
}
