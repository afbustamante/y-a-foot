package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.services.CarSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarSearchServiceImpl implements CarSearchService {

    @Autowired
    private VoitureDAO voitureDAO;

    @Override
    public List<Voiture> findCarsByPlayer(Joueur player, Contexte userContext) throws DatabaseException {
        return voitureDAO.chargerVoituresJoueur(player);
    }
}
