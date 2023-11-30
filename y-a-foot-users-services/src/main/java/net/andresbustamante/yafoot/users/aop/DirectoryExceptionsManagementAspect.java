package net.andresbustamante.yafoot.users.aop;

import jakarta.ws.rs.WebApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.text.MessageFormat;

/**
 * Aspect in charge of transforming any exception thrown when contacting the user directory into a
 * {@link DirectoryException}.
 */
@Aspect
@Component
public class DirectoryExceptionsManagementAspect {

    /**
     * Pointcut for services implementation using the active directory.
     */
    @Pointcut("execution(* net.andresbustamante.yafoot.users.services.impl.*ServiceImpl.*(..))")
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
    public Object transformException(final ProceedingJoinPoint pjp) throws Throwable {
        Object returnedObject;

        try {
            returnedObject = pjp.proceed();
        } catch (HttpClientErrorException | HttpServerErrorException | WebApplicationException e) {
            String message = MessageFormat.format("User directory error when processing the request for {0}",
                    pjp.getSignature().toShortString());
            throw new DirectoryException(message + System.lineSeparator() + e.getMessage());
        }
        return returnedObject;
    }
}
