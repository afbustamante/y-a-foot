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
    public void filtrerMethodesServices() {
        // no-op
    }

    @Around("filtrerMethodesServices()")
    public Object transformerExceptions(ProceedingJoinPoint pjp) throws Throwable {
        Object retour;

        try {
            retour = pjp.proceed();
        } catch (DataAccessException | ConnectException | SQLException e) {
            // Exceptions de base de données
            String message = MessageFormat.format("Erreur de base de données lors du traitement de la " +
                    "demande sur {0}", pjp.getSignature().toShortString());
            throw new DatabaseException(message + System.lineSeparator() + e.getMessage());
        } catch (NamingException e) {
            // Exceptions de l'annuaire LDAP
            String message = MessageFormat.format("Erreur de l'annuaire LDAP lors du traitement de la " +
                    "demande sur {0}", pjp.getSignature().toShortString());
            throw new LdapException(message + System.lineSeparator() + e.getMessage());
        }
        return retour;
    }
}
