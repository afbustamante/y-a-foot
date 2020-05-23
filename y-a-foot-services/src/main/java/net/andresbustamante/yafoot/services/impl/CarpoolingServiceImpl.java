package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.AuthorisationException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.CarpoolingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarpoolingServiceImpl implements CarpoolingService {

    private CarDAO carDAO;

    private MatchDAO matchDAO;

    private final Logger log = LoggerFactory.getLogger(CarpoolingServiceImpl.class);

    @Autowired
    public CarpoolingServiceImpl(CarDAO carDAO, MatchDAO matchDAO) {
        this.carDAO = carDAO;
        this.matchDAO = matchDAO;
    }

    @Transactional
    @Override
    public void updateCarpoolingInformation(Match match, Player player, Car car, boolean isCarConfirmed, UserContext ctx)
            throws DatabaseException, ApplicationException {
        if (car.getId() != null) {
            Car storedCar = carDAO.findCarById(car.getId());

            if (storedCar != null && storedCar.getDriver().getEmail().equals(ctx.getUsername())) {
                // The user is the owner of the car
                matchDAO.updateCarForRegistration(match, player, car, isCarConfirmed);

                log.info("Carpool update for match #{}: Player #{} confirmation modified for car #{}", match.getId(),
                        player.getId(), car.getId());
            } else {
                throw new AuthorisationException("User not allowed to update carpooling details for registration");
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void processCarSeatRequest(Match match, Player player, Car car, UserContext ctx)
            throws DatabaseException, ApplicationException {
        // TODO Implement this method
    }
}
