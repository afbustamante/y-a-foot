package net.andresbustamante.yafoot.auth.services.impl;

import net.andresbustamante.yafoot.auth.exceptions.UserNotAuthorisedException;
import net.andresbustamante.yafoot.auth.model.PasswordResetDetails;
import net.andresbustamante.yafoot.auth.model.enums.RolesEnum;
import net.andresbustamante.yafoot.auth.repository.UserRepository;
import net.andresbustamante.yafoot.auth.services.UserManagementService;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import net.andresbustamante.yafoot.commons.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.MessagingService;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Locale;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private static final int TOKEN_SIZE = 16;

    private final Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @Value("${web.public.password-reset.url}")
    private String passwordResetUrl;

    private UserRepository userRepository;

    private MessagingService messagingService;

    private final RandomStringGenerator tokenGenerator = new RandomStringGenerator.Builder().withinRange('0', '9').build();

    @Autowired
    public UserManagementServiceImpl(UserRepository userRepository, MessagingService messagingService) {
        this.userRepository = userRepository;
        this.messagingService = messagingService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createUser(User user, RolesEnum role, UserContext ctx) throws LdapException {
        userRepository.saveUser(user, role);
        log.info("User {} created", user.getEmail());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUser(User user, UserContext ctx) throws LdapException {
        userRepository.updateUser(user);
        log.info("Details updated for user {}", user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserPassword(User user, UserContext ctx) throws LdapException, ApplicationException {
        if (!user.getEmail().equals(ctx.getUsername())) {
            throw new UserNotAuthorisedException("User not allowed to modify password from another user");
        }

        userRepository.updatePassword(user);
        log.info("Password updated for user {}", user.getEmail());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUser(User user, UserContext ctx) throws LdapException {
        userRepository.deleteUser(user);
        log.info("User {} deleted", user.getEmail());
    }

    @Transactional
    @Override
    public String createPasswordResetToken(User user) throws LdapException, ApplicationException {
        String token;
        boolean isExistingToken;

        do {
            token = generatePasswordResetToken();
            isExistingToken = userRepository.findUserByToken(token) != null;
        } while (isExistingToken);

        userRepository.saveTokenForUser(token, user);
        notifyTokenLink(token, user);
        log.info("New password-reset token created for user {}", user.getEmail());
        return token;
    }

    @Transactional
    @Override
    public void resetUserPassword(User user, String passwordResetToken) throws LdapException, ApplicationException {
        User tokenUser = userRepository.findUserByToken(passwordResetToken);

        if (tokenUser != null && tokenUser.equals(user)) {
            updateUserPassword(user, new UserContext(user.getEmail()));
            userRepository.removeTokenForUser(user);
            log.info("Password reset for user {}", user.getEmail());
        } else {
            throw new UserNotAuthorisedException("Invalid token used to reset password");
        }
    }

    /**
     * Sends an email to the user that asked for resetting his/her password with a link to access the web application
     *
     * @param token Token to use in the message
     * @param user User to notify
     * @throws ApplicationException If an invalid template is used to send the notification
     */
    private void notifyTokenLink(String token, User user) throws ApplicationException {
        String[] params = {};
        String link = MessageFormat.format(passwordResetUrl, token);
        String template = "password-reset-email_" + user.getPreferredLanguage() + ".ftl";
        messagingService.sendEmail(user.getEmail(), "password.reset.email.subject", params, template,
                new PasswordResetDetails(user.getFirstName(), link), new Locale(user.getPreferredLanguage()));
    }

    private String generatePasswordResetToken() {
        return tokenGenerator.generate(TOKEN_SIZE);
    }
}
