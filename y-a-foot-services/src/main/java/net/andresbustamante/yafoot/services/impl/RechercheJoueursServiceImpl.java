package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.RechercheJoueursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author andresbustamante
 */
@Service
public class RechercheJoueursServiceImpl implements RechercheJoueursService {

    @Autowired
    private JoueurDAO joueurDAO;

    @Override
    public Joueur chercherJoueur(String email, Contexte contexte) throws BDDException {
        try {
            return joueurDAO.chercherJoueurParEmail(email);
        } catch (SQLException | DataAccessException e) {
            throw new BDDException(e.getMessage());
        }
    }
}
