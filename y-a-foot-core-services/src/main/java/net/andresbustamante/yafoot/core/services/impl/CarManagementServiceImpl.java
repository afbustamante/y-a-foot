package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
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

    @Override
    @Transactional
    public void updateCar(Integer carId, Car updatedCar, UserContext ctx)
            throws DatabaseException, ApplicationException {
        Player player = playerDAO.findPlayerByEmail(ctx.getUsername());
        Car storedCar = carDAO.findCarById(carId);

        if (storedCar != null) {
            if (player != null && !storedCar.getDriver().getId().equals(player.getId())) {
                throw new ApplicationException("unauthorised.user.error", "This car can only be updated by its owner");
            }

            storedCar.setName(updatedCar.getName());
            storedCar.setNumSeats(updatedCar.getNumSeats());

            carDAO.updateCar(storedCar);

            log.info("Car {} successfully updated", carId);
        }
    }

    @Override
    @Transactional
    public void deactivateCar(Car car, UserContext ctx) throws ApplicationException, DatabaseException {
        Player player = playerDAO.findPlayerByEmail(ctx.getUsername());

        if (player != null && !car.getDriver().getId().equals(player.getId())) {
            throw new ApplicationException("unauthorised.user.error", "This car can only be deactivated by its"
                    + " owner");
        }

        if (carDAO.isCarUsedForComingMatches(car)) {
            throw new ApplicationException("car.registered.coming.match.error",
                    "This car cannot be deactivated because it is still registered for a coming match");
        }

        carDAO.deactivateCar(car);

        log.info("Car {} successfully deactivated", car.getId());
    }

    @Transactional
    @Override
    public void deactivateCarsByPlayer(Player player, UserContext ctx) throws DatabaseException {
        int numCars = carDAO.deactivateCarsByPlayer(player);
        log.info("{} cars removed for player", numCars);
    }
}
