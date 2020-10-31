package net.andresbustamante.yafoot.aop;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.NamingException;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.sql.SQLException;
import java.text.MessageFormat;

@Aspect
@Component
public class ExceptionsManagementAspect {

    @Pointcut("execution(* net.andresbustamante.yafoot.services.impl.*ServiceImpl.*(..))")
    public void filterServicesMethods() {
        // no-op
    }

    @Around("filterServicesMethods()")
    public Object transformException(ProceedingJoinPoint pjp) throws Throwable {
        Object returnedObject;

        try {
            returnedObject = pjp.proceed();
        } catch (DataAccessException | ConnectException | SQLException e) {
            // Database exceptions
            String message = MessageFormat.format("Database error when processing the request for {0}",
                    pjp.getSignature().toShortString());
            throw new DatabaseException(message + System.lineSeparator() + e.getMessage());
        } catch (NamingException e) {
            // LDAP directory exceptions
            String message = MessageFormat.format("LDAP directory error when processing the request for {0}",
                    pjp.getSignature().toShortString());
            throw new LdapException(message + System.lineSeparator() + e.getMessage());
        }
        return returnedObject;
    }
}
