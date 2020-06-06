package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.ldap.UserRepository;
import net.andresbustamante.yafoot.model.PasswordResetDetails;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.MessagingService;
import net.andresbustamante.yafoot.services.UserManagementService;
import org.apache.commons.text.RandomStringGenerator;
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
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUser(User user, UserContext ctx) throws LdapException {
        userRepository.updateUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserPassword(User user, UserContext ctx) throws LdapException {
        userRepository.updatePassword(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUser(User user, UserContext ctx) throws LdapException {
        userRepository.deleteUser(user);
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
        return token;
    }

    /**
     * Sends an email to the user that asked for resetting his/her password with a link to access the web application
     *
     * @param token Token to use in the message
     * @param user User to notify
     * @throws ApplicationException
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
