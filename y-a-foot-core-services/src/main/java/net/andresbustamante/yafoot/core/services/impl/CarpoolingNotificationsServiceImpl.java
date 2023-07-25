package net.andresbustamante.yafoot.core.services.impl;

import freemarker.template.TemplateException;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.utils.TemplateUtils;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.CarpoolingRequest;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.CarpoolingNotificationsService;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class CarpoolingNotificationsServiceImpl implements CarpoolingNotificationsService {

    private final MatchDao matchDao;
    private final PlayerDao playerDao;
    private final CarDao carDao;
    private final MessageSource messageSource;
    private final TemplateUtils templateUtils;
    private final MessagingService messagingService;

    @Value("${api.matches.one.path}")
    private String matchManagementUrl;

    @Value("${app.web.public.carpooling-management.url}")
    private String carpoolingManagementUrl;

    public CarpoolingNotificationsServiceImpl(
            MatchDao matchDao, PlayerDao playerDao, CarDao carDao,
            MessageSource messageSource, TemplateUtils templateUtils, MessagingService messagingService) {
        this.matchDao = matchDao;
        this.playerDao = playerDao;
        this.carDao = carDao;
        this.messageSource = messageSource;
        this.templateUtils = templateUtils;
        this.messagingService = messagingService;
    }

    @Override
    @Transactional
    public void notifyCarpoolingRequest(Integer playerId, Integer matchId, Integer carId) throws ApplicationException {
        Player player = playerDao.findPlayerById(playerId);
        Match match = matchDao.findMatchById(matchId);
        Car car = carDao.findCarById(carId);

        Player driver = car.getDriver();
        String template = "carpooling-request-email_" + driver.getPreferredLanguage() + ".ftl";
        String link = String.format(carpoolingManagementUrl, match.getCode());
        Locale locale = new Locale(driver.getPreferredLanguage());
        String matchDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", locale).format(match.getDate());

        CarpoolingRequest request = new CarpoolingRequest();
        request.setRequesterFirstName(player.getFirstName());
        request.setDriverFirstName(driver.getFirstName());
        request.setLink(link);
        request.setMatchDate(matchDate);

        try {
            String content = templateUtils.getContent(template, request);
            String subject = messageSource.getMessage("carpool.request.email.subject",
                    new String[]{player.getFirstName()}, locale);

            messagingService.sendEmail(driver.getEmail(), subject, content);
        } catch (IOException | TemplateException e) {
            throw new ApplicationException("invalid.template.error", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void notifyCarpoolingUpdate(Integer playerId, Integer matchId, Integer carId, boolean isCarSeatConfirmed)
            throws ApplicationException {
        Player player = playerDao.findPlayerById(playerId);
        Match match = matchDao.findMatchById(matchId);
        Car car = carDao.findCarById(carId);

        String preferredLanguage = player.getPreferredLanguage();
        Locale playerLocale = preferredLanguage != null ? new Locale(preferredLanguage) : Locale.getDefault();

        String matchDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", playerLocale).format(match.getDate());
        String link = String.format(matchManagementUrl, match.getCode());

        CarpoolingRequest request = new CarpoolingRequest();
        request.setRequesterFirstName(player.getFirstName());
        request.setDriverFirstName(car.getDriver().getFirstName());
        request.setLink(link);
        request.setMatchDate(matchDate);

        try {
            String confirmationTemplate = "carpooling-confirmation-email_" + playerLocale.getLanguage().toLowerCase()
                    + ".ftl";
            String rejectionTemplate = "carpooling-rejection-email_" + playerLocale.getLanguage().toLowerCase()
                    + ".ftl";
            String template = isCarSeatConfirmed ? confirmationTemplate : rejectionTemplate;
            String content = templateUtils.getContent(template, request);

            String s = isCarSeatConfirmed ? "carpool.confirmation.email.subject" : "carpool.rejection.email.subject";
            String subject = messageSource.getMessage(s, null, playerLocale);

            messagingService.sendEmail(player.getEmail(), subject, content);
        } catch (TemplateException | IOException e) {
            throw new ApplicationException("invalid.template.error", e.getMessage());
        }
    }
}
