package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.CarDAO;
import net.andresbustamante.yafoot.core.dao.MatchDAO;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.*;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class CarpoolingServiceImpl implements CarpoolingService {

    private final CarDAO carDAO;
    private final MatchDAO matchDAO;
    private final MessagingService messagingService;

    @Value("${app.web.public.carpooling-management.url}")
    private String carpoolingManagementUrl;

    @Value("${app.web.public.match-management.url}")
    private String matchManagementUrl;

    private final Logger log = LoggerFactory.getLogger(CarpoolingServiceImpl.class);

    @Autowired
    public CarpoolingServiceImpl(CarDAO carDAO, MatchDAO matchDAO, MessagingService messagingService) {
        this.carDAO = carDAO;
        this.matchDAO = matchDAO;
        this.messagingService = messagingService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Car> findAvailableCarsByMatch(Match match) throws DatabaseException {
        if (match.isCarpoolingEnabled()) {
            return carDAO.findCarsByMatch(match);
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public void updateCarpoolingInformation(Match match, Player player, Car car, boolean isCarConfirmed, UserContext ctx)
            throws DatabaseException, ApplicationException {
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
                    processCarSeatUpdate(match, player, car, isCarConfirmed);
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
    public void processCarSeatRequest(Match match, Player player, Car car, UserContext ctx)
            throws DatabaseException, ApplicationException {
        String template = "carpooling-request-email_" + car.getDriver().getPreferredLanguage() + ".ftl";
        String link = MessageFormat.format(carpoolingManagementUrl, match.getCode());
        Locale locale = new Locale(car.getDriver().getPreferredLanguage());
        String matchDate = match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", locale));

        CarpoolingRequest request = new CarpoolingRequest();
        request.setRequesterFirstName(player.getFirstName());
        request.setDriverFirstName(car.getDriver().getFirstName());
        request.setLink(link);
        request.setMatchDate(matchDate);

        messagingService.sendEmail(car.getDriver().getEmail(), "carpool.request.email.subject", new String[]{player.getFirstName()},
                template, request, locale);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void processTransportationChange(Match match, Car oldCar, Car newCar, UserContext ctx) throws ApplicationException {
        List<Registration> registrations = matchDAO.findPassengerRegistrationsByCar(match, oldCar);
        Car storedNewCar = (newCar != null && newCar.getId() != null) ? carDAO.findCarById(newCar.getId()) : null;

        if (CollectionUtils.isNotEmpty(registrations)) {
            if (storedNewCar != null && storedNewCar.getDriver().getEmail().equals(ctx.getUsername())) {
                // Changing between two owned cars
                if (storedNewCar.getNumSeats() >= registrations.size()) {
                    // Transfer the passengers to the new car
                    registrations.forEach(registration -> matchDAO.updateCarForRegistration(match, registration.getPlayer(),
                            storedNewCar, registration.isCarConfirmed()));
                } else {
                    throw new ApplicationException("carpooling.passengers.transfer.failed", "There are not enough seats for all your passengers");
                }
            } else if (storedNewCar == null && newCar != null) {
                // Changing to an unregistered car
                throw new ApplicationException("carpooling.passengers.transfer.failed", "The new car must have been registered before this operation");
            } else {
                // Changing to a car belonging to somebody else or not using a car at all
                registrations.forEach(registration -> matchDAO.resetCarDetails(match, registration.getPlayer()));
            }
        }
    }

    private void processCarSeatUpdate(Match match, Player player, Car car, boolean isCarSeatConfirmed)
            throws ApplicationException {
        String confirmationTemplate = "carpooling-confirmation-email_" + player.getPreferredLanguage() + ".ftl";
        String rejectionTemplate = "carpooling-rejection-email_" + player.getPreferredLanguage() + ".ftl";
        String template = (isCarSeatConfirmed) ? confirmationTemplate : rejectionTemplate;
        String subject = (isCarSeatConfirmed) ? "carpool.confirmation.email.subject" : "carpool.rejection.email.subject";

        String link = MessageFormat.format(matchManagementUrl, match.getCode());
        Locale locale = new Locale(player.getPreferredLanguage());
        String matchDate = match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", locale));

        CarpoolingRequest request = new CarpoolingRequest();
        request.setRequesterFirstName(player.getFirstName());
        request.setDriverFirstName(car.getDriver().getFirstName());
        request.setLink(link);
        request.setMatchDate(matchDate);

        messagingService.sendEmail(car.getDriver().getEmail(), subject, null, template, request, locale);
    }
}
