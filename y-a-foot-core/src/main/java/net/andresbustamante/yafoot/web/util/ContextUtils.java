package net.andresbustamante.yafoot.web.util;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.UserContext;

import javax.servlet.http.HttpServletRequest;

public class ContextUtils {

    private ContextUtils() {}

    public static UserContext getUserContext(HttpServletRequest request) throws ApplicationException {
        String id = request.getHeader(UserContext.USER_CTX);

        if (id != null) {
            Integer userId = (request.getHeader(UserContext.USER_CTX) != null) ? Integer.valueOf(id) : null;
            return new UserContext(userId);
        } else {
            throw new ApplicationException("Unable to find user's ID");
        }
    }

}
