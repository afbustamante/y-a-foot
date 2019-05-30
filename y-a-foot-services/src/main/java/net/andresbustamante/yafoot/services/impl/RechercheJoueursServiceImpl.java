package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.RechercheJoueursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andresbustamante
 */
@Service
public class RechercheJoueursServiceImpl implements RechercheJoueursService {

    @Autowired
    private JoueurDAO joueurDAO;

    @Override
    public Joueur chercherJoueur(String email, Contexte contexte) throws DatabaseException {
        return joueurDAO.chercherJoueurParEmail(email);
    }
}
