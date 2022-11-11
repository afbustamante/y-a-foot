package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarManagementServiceImpl implements CarManagementService {

    private final Logger log = LoggerFactory.getLogger(CarManagementServiceImpl.class);

    private final CarDao carDAO;
    private final PlayerDao playerDAO;

    @Autowired
    public CarManagementServiceImpl(CarDao carDAO, PlayerDao playerDAO) {
        this.carDAO = carDAO;
        this.playerDAO = playerDAO;
    }

    @Transactional
    @Override
    public Integer saveCar(Car car, UserContext ctx) throws DatabaseException {
        Player player = playerDAO.findPlayerByEmail(ctx.getUsername());
        car.setDriver(player);
        carDAO.saveCar(car);
        log.info("Car successfully added with the ID {}", car.getId());
        return car.getId();
    }

    @Transactional
    @Override
    public void deleteCarsByPlayer(Player player, UserContext ctx) throws DatabaseException {
        int numCars = carDAO.deleteCarsByPlayer(player);
        log.info("{} cars removed for player", numCars);
    }
}
