package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.services.CarSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarSearchServiceImpl implements CarSearchService {

    @Autowired
    private CarDAO carDAO;

    @Override
    public List<Voiture> findCarsByPlayer(Joueur player, UserContext userContext) throws DatabaseException {
        return carDAO.findCarsByPlayer(player);
    }
}
