package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.services.CarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarManagementServiceImpl implements CarManagementService {

    @Autowired
    private CarDAO carDAO;

    @Override
    public int saveCar(Voiture voiture, UserContext ctx) throws DatabaseException {
        carDAO.saveCar(voiture, new Joueur(ctx.getUserId()));
        return voiture.getId();
    }
}
