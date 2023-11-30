package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.CarSearchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CarSearchServiceImpl implements CarSearchService {

    private final CarDao carDAO;
    private final PlayerDao playerDao;

    public CarSearchServiceImpl(final CarDao carDAO, final PlayerDao playerDao) {
        this.carDAO = carDAO;
        this.playerDao = playerDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Car> findCars(final UserContext ctx) {
        Player player = playerDao.findPlayerByEmail(ctx.getUsername());
        return player != null ? carDAO.findCarsByPlayer(player) : Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public Car loadCar(final Integer id, final UserContext ctx) throws ApplicationException {
        Car car = carDAO.findCarById(id);

        if (car != null) {
            if (car.getDriver() != null && car.getDriver().getEmail().equals(ctx.getUsername())) {
                return car;
            } else {
                throw new UnauthorisedUserException("Actual user is not allowed to load the details for a car");
            }
        }
        return null;
    }
}
