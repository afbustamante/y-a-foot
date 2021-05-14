package net.andresbustamante.yafoot.auth.aop;

import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.ldap.NamingException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
public class AuthExceptionsManagementAspect {

    @Pointcut("execution(* net.andresbustamante.yafoot.auth.services.impl.*ServiceImpl.*(..))")
    public void filterServicesMethods() {
        // no-op
    }

    @Around("filterServicesMethods()")
    public Object transformException(ProceedingJoinPoint pjp) throws Throwable {
        Object returnedObject;

        try {
            returnedObject = pjp.proceed();
        } catch (NamingException e) {
            // LDAP directory exceptions
            String message = MessageFormat.format("LDAP directory error when processing the request for {0}",
                    pjp.getSignature().toShortString());
            throw new LdapException(message + System.lineSeparator() + e.getMessage());
        }
        return returnedObject;
    }
}
