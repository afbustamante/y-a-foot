package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.CarManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarManagementServiceImpl implements CarManagementService {

    private final Logger log = LoggerFactory.getLogger(CarManagementServiceImpl.class);

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private PlayerDAO playerDAO;

    @Transactional
    @Override
    public int saveCar(Car car, UserContext ctx) throws DatabaseException {
        Player player = playerDAO.findPlayerByEmail(ctx.getUsername());
        carDAO.saveCar(car, player);
        return car.getId();
    }

    @Transactional
    @Override
    public void deleteCarsByPlayer(Player player, UserContext ctx) throws DatabaseException {
        int numCars = carDAO.deleteCarsByPlayer(player);
        log.info("{} cars removed for player", numCars);
    }
}
