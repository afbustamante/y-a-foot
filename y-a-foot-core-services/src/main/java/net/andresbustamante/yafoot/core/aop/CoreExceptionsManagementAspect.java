package net.andresbustamante.yafoot.core.aop;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.sql.SQLException;
import java.text.MessageFormat;

@Aspect
@Component
public class CoreExceptionsManagementAspect {

    /**
     * Pointcut for core service methods.
     */
    @Pointcut("execution(* net.andresbustamante.yafoot.core.services.impl.*ServiceImpl.*(..))")
    public void filterServicesMethods() {
        // no-op
    }

    /**
     * Transform database related exceptions into {@link DatabaseException}.
     *
     * @param pjp Join point for a core method
     * @return Method return
     * @throws Throwable Method exception thrown
     */
    @Around("filterServicesMethods()")
    public Object transformException(final ProceedingJoinPoint pjp) throws Throwable {
        Object returnedObject;

        try {
            returnedObject = pjp.proceed();
        } catch (DataAccessException | ConnectException | SQLException e) {
            // Database exceptions
            String message = MessageFormat.format("Database error when processing the request for {0}",
                    pjp.getSignature().toShortString());
            throw new DatabaseException(message + System.lineSeparator() + e.getMessage());
        }
        return returnedObject;
    }
}
