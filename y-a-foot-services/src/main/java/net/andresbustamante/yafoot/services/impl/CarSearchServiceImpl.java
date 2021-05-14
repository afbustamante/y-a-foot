package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.UserNotAuthorisedException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.CarSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarSearchServiceImpl implements CarSearchService {

    private CarDAO carDAO;

    @Autowired
    public CarSearchServiceImpl(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Car> findCarsByPlayer(Player player) throws DatabaseException {
        return carDAO.findCarsByPlayer(player);
    }

    @Transactional(readOnly = true)
    @Override
    public Car loadCar(Integer id, UserContext ctx) throws DatabaseException, ApplicationException {
        Car car = carDAO.findCarById(id);

        if (car != null) {
            if (car.getDriver() != null && car.getDriver().getEmail().equals(ctx.getUsername())) {
                return car;
            } else {
                throw new UserNotAuthorisedException("Actual user is not allowed to load the " +
                        "details for a car");
            }
        }
        return null;
    }
}
