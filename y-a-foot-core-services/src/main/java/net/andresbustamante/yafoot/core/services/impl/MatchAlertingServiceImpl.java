package net.andresbustamante.yafoot.core.services.impl;

import freemarker.template.TemplateException;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.utils.TemplateUtils;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.MatchAlert;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.MatchAlertingService;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class MatchAlertingServiceImpl implements MatchAlertingService {

    private final MatchDao matchDao;
    private final MessageSource messageSource;
    private final TemplateUtils templateUtils;
    private final MessagingService messagingService;

    public MatchAlertingServiceImpl(
            final MatchDao matchDao, final MessageSource messageSource, final TemplateUtils templateUtils,
            final MessagingService messagingService) {
        this.matchDao = matchDao;
        this.messageSource = messageSource;
        this.templateUtils = templateUtils;
        this.messagingService = messagingService;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void checkForAlertsAfterPlayerRemovedFromMatch(final Integer matchId, final Integer playerId)
            throws ApplicationException {
        Match match = matchDao.findMatchById(matchId);

        // A match is impacted when the last player before arriving to the lowest number of players expected quits
        boolean isMatchImpacted = match.getNumRegisteredPlayers().equals(match.getNumPlayersMin());

        if (isMatchImpacted) {
            try {
                // Send an alert as the match has a number of players below the minimum expected now
                sendAlertMessage(match);
            } catch (IOException | TemplateException e) {
                throw new ApplicationException("invalid.template.error", e.getMessage());
            }
        }
    }

    /**
     * Sends an alert message to the player who created the match to prevent him from an action leading a match to an
     * imminent danger of being cancelled.
     *
     * @param match The match concerned by the alert
     */
    private void sendAlertMessage(final Match match) throws TemplateException, IOException {
        Player creator = match.getCreator();
        String language = creator.getPreferredLanguage();
        Locale locale = new Locale(language);
        String template = "match-cancel-risk-alert-email_" + language + ".ftl";
        String[] parameters = {match.getCode()};

        MatchAlert alert = new MatchAlert();
        alert.setCreatorFirstName(creator.getFirstName());
        alert.setMatchDate(match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", locale)));
        alert.setNumMinPlayers(match.getNumPlayersMin());

        try {
            String subject = messageSource.getMessage("match.cancel.risk.alert.subject", parameters, locale);
            String content = templateUtils.getContent(template, alert);

            messagingService.sendEmail(creator.getEmail(), subject, content);
        } catch (ApplicationException e) {
            throw new RuntimeException(e);
        }
    }
}
