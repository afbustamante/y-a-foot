package net.andresbustamante.yafoot.aop;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.NamingException;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.sql.SQLException;

@Aspect
@Component
public class GestionExceptionsAspect {

    private static final String DETAIL_EXCEPTION_MESSAGE = "Détail de l'exception:";
    private final Logger log = LoggerFactory.getLogger(GestionExceptionsAspect.class);

    @Pointcut("execution(* net.andresbustamante.yafoot.services.impl.*ServiceImpl.*(..))")
    public void filtrerMethodesServices() {
        // no-op
    }

    @Around("filtrerMethodesServices()")
    public Object transformerExceptions(ProceedingJoinPoint pjp) throws Throwable {
        Object retour;

        try {
            retour = pjp.proceed();
        } catch (DataAccessException | SQLException e) {
            log.error("Erreur de base de données lors du traitement de la demande sur {}", pjp.getSignature().toShortString());
            log.error(DETAIL_EXCEPTION_MESSAGE, e);
            throw new DatabaseException(e.getMessage());
        } catch (NamingException e) {
            log.error("Erreur de l'annuaire LDAP lors du traitement de la demande sur {}", pjp.getSignature().toShortString());
            log.error(DETAIL_EXCEPTION_MESSAGE, e);
            throw new LdapException(e.getMessage());
        } catch (ConnectException e) {
            log.error("Erreur de disponibilité du service lors du traitement de la demande sur {}", pjp.getSignature().toShortString());
            log.error(DETAIL_EXCEPTION_MESSAGE, e);
            throw new DatabaseException(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur inconnue ou non maîtrisée lors du traitement de la demande sur {}", pjp.getSignature().toShortString());
            log.error(DETAIL_EXCEPTION_MESSAGE, e);
            throw new DatabaseException(e.getMessage());
        }
        return retour;
    }
}
