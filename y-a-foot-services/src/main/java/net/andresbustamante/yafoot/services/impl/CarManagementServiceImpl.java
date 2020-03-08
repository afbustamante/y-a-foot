package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
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

    @Autowired
    private PlayerDAO playerDAO;

    @Override
    public int saveCar(Voiture voiture, UserContext ctx) throws DatabaseException {
        Joueur player = playerDAO.findPlayerByEmail(ctx.getUsername());
        carDAO.saveCar(voiture, player);
        return voiture.getId();
    }
}
