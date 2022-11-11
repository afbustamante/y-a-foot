package net.andresbustamante.yafoot.auth.aop;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.ldap.NamingException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Aspect in charge of transforming any exception thrown when contacting the LDAP tree into a
 * {@link DirectoryException}.
 */
@Aspect
@Component
public class LdapExceptionsManagementAspect {

    /**
     * Pointcut for services implementation using the LDAP tree.
     */
    @Pointcut("execution(* net.andresbustamante.yafoot.auth.services.impl.*ServiceImpl.*(..))")
    public void filterServicesMethods() {
        // no-op
    }

    /**
     * Transforms any exception thrown into a DirectoryException.
     *
     * @param pjp Join point used for the method to intercept
     * @return Service method return
     * @throws Throwable Exception from service method
     */
    @Around("filterServicesMethods()")
    public Object transformException(ProceedingJoinPoint pjp) throws Throwable {
        Object returnedObject;

        try {
            returnedObject = pjp.proceed();
        } catch (NamingException e) {
            // LDAP directory exceptions
            String message = MessageFormat.format("LDAP directory error when processing the request for {0}",
                    pjp.getSignature().toShortString());
            throw new DirectoryException(message + System.lineSeparator() + e.getMessage());
        }
        return returnedObject;
    }
}
