package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.events.CarpoolingRequestEvent;
import net.andresbustamante.yafoot.core.events.CarpoolingUpdateEvent;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Registration;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CarpoolingServiceImpl implements CarpoolingService {

    private final CarDao carDAO;
    private final MatchDao matchDAO;

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.queues.carpooling.requests.name}")
    private String carpoolingRequestsQueue;

    @Value("${app.messaging.queues.carpooling.updates.name}")
    private String carpoolingUpdatesQueue;

    private final Logger log = LoggerFactory.getLogger(CarpoolingServiceImpl.class);

    public CarpoolingServiceImpl(final CarDao carDAO, final MatchDao matchDAO, final RabbitTemplate rabbitTemplate) {
        this.carDAO = carDAO;
        this.matchDAO = matchDAO;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Car> findAvailableCarsByMatch(final Match match) throws DatabaseException {
        if (match.isCarpoolingEnabled()) {
            return carDAO.findCarsByMatch(match);
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public void updateCarpoolingInformation(final Match match, final Player player, final Car car,
                                            final boolean isCarConfirmed, final UserContext ctx)
            throws ApplicationException {
        if (car.getId() != null) {
            Car storedCar = carDAO.findCarById(car.getId());

            if (storedCar != null && storedCar.getDriver().getEmail().equals(ctx.getUsername())) {
                // The user is the owner of the car
                Registration actualPlayerRegistration = null;

                if (!player.equals(car.getDriver())) {
                    actualPlayerRegistration = matchDAO.loadRegistration(match, player);
                }

                matchDAO.updateCarForRegistration(match, player, car, isCarConfirmed);

                if (actualPlayerRegistration != null && actualPlayerRegistration.isCarConfirmed() != isCarConfirmed) {
                    // Send notification for this update
                    processCarSeatUpdate(match, player, storedCar, isCarConfirmed);
                }

                log.info("Carpool update for match #{}: Player #{} confirmation modified for car #{}", match.getId(),
                        player.getId(), car.getId());
            } else if (storedCar == null) {
                throw new ApplicationException("car.not.found.error", "No car found for the given ID");
            } else {
                throw new UnauthorisedUserException("User not allowed to update carpooling details for registration");
            }
        } else {
            throw new ApplicationException("car.not.found.error", "No car found as no valid ID was used");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void processCarSeatRequest(final Match match, final Player player, final Car car, final UserContext ctx) {
        CarpoolingRequestEvent event = new CarpoolingRequestEvent();
        event.setPlayerId(player.getId());
        event.setPlayerFirstName(player.getFirstName());
        event.setMatchId(match.getId());
        event.setMatchCode(match.getCode());
        event.setCarId(car.getId());
        event.setCarName(car.getName());

        rabbitTemplate.convertAndSend(carpoolingRequestsQueue, event);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void processTransportationChange(final Match match, final Car oldCar, final Car newCar,
                                            final UserContext ctx)
            throws ApplicationException {
        List<Registration> registrations = matchDAO.findPassengerRegistrationsByCar(match, oldCar);
        Car storedNewCar = (newCar != null && newCar.getId() != null) ? carDAO.findCarById(newCar.getId()) : null;

        if (CollectionUtils.isNotEmpty(registrations)) {
            if (storedNewCar != null && storedNewCar.getDriver().getEmail().equals(ctx.getUsername())) {
                // Changing between two owned cars
                if (storedNewCar.getNumSeats() >= registrations.size()) {
                    // Transfer the passengers to the new car
                    registrations.forEach(registration -> matchDAO.updateCarForRegistration(match,
                            registration.getPlayer(), storedNewCar, registration.isCarConfirmed()));
                } else {
                    throw new ApplicationException("carpooling.passengers.transfer.failed",
                            "There are not enough seats for all your passengers");
                }
            } else if (storedNewCar == null && newCar != null) {
                // Changing to an unregistered car
                throw new ApplicationException("carpooling.passengers.transfer.failed",
                        "The new car must have been registered before this operation");
            } else {
                // Changing to a car belonging to somebody else or not using a car at all
                registrations.forEach(registration -> matchDAO.resetCarDetails(match, registration.getPlayer()));
            }
        }
    }

    /**
     * Sends a notification concerning an update on the carpooling request made by a player for a given car to go to a
     * given match.
     *
     * @param match Match to go to
     * @param player Player asking for a seat in carpooling
     * @param car Selected car
     * @param isCarSeatConfirmed Confirmation / refusal from the driver of the given car
     */
    private void processCarSeatUpdate(final Match match, final Player player, final Car car,
                                      final boolean isCarSeatConfirmed) {
        CarpoolingUpdateEvent event = new CarpoolingUpdateEvent();
        event.setCarId(car.getId());
        event.setCarName(car.getName());
        event.setMatchId(match.getId());
        event.setMatchCode(match.getCode());
        event.setPlayerId(player.getId());
        event.setPlayerFirstName(player.getFirstName());
        event.setCarSeatConfirmed(isCarSeatConfirmed ? 1 : 0);

        rabbitTemplate.convertAndSend(carpoolingUpdatesQueue, event);
    }
}
