package net.andresbustamante.yafoot.dao;

import net.andresbustamante.framework.dao.DAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Match;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

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
    boolean isCodeExistant(String codeMatch) throws BDDException;

    /**
     *
     * @param codeMatch
     * @return
     */
    Match chercherParCode(String codeMatch) throws BDDException;

    /**
     *
     * @param idJoueur
     * @return
     * @throws BDDException
     */
    List<Match> chercherParJoueur(Integer idJoueur, Date dateAuPlusTot) throws BDDException;
}
