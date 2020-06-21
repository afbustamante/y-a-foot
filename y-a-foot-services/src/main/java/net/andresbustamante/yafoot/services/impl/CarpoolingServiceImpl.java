package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.AuthorisationException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.CarpoolingService;
import net.andresbustamante.yafoot.services.MessagingService;
import net.andresbustamante.yafoot.util.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class CarpoolingServiceImpl implements CarpoolingService {

    private CarDAO carDAO;

    private MatchDAO matchDAO;

    private MessagingService messagingService;

    @Value("${web.public.carpooling-management.url}")
    private String carpoolingManagementUrl;

    private final Logger log = LoggerFactory.getLogger(CarpoolingServiceImpl.class);

    @Autowired
    public CarpoolingServiceImpl(CarDAO carDAO, MatchDAO matchDAO, MessagingService messagingService) {
        this.carDAO = carDAO;
        this.matchDAO = matchDAO;
        this.messagingService = messagingService;
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
        String template = "carpooling-request-email_" + player.getPreferredLanguage() + ".ftl";
        String link = MessageFormat.format(carpoolingManagementUrl, match.getCode());
        String matchDate = match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale(car.getDriver().getPreferredLanguage())));

        CarpoolingRequest request = new CarpoolingRequest();
        request.setRequesterFirstName(player.getFirstName());
        request.setDriverFirstName(car.getDriver().getFirstName());
        request.setLink(link);
        request.setMatchDate(matchDate);

        messagingService.sendEmail(car.getDriver().getEmail(), "carpool.request.email.subject", new String[]{player.getFirstName()},
                template, request, LocaleUtils.DEFAULT_LOCALE);
    }
}
