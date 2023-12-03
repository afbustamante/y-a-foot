package net.andresbustamante.yafoot.commons.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect that controls logging on main services.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Logger to use for this class.
     */
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut for service implementations.
     */
    @Pointcut("execution(* net.andresbustamante.yafoot.*.services.impl.*ServiceImpl.*(..))")
    public void filterServicesMethods() {
        // no-op
    }

    /**
     * Adds a log for every call on a service public method.
     *
     * @param joinPoint Join point for the called method
     */
    @Before("filterServicesMethods()")
    public void logServicesMethodsStart(final JoinPoint joinPoint) {
        if (joinPoint != null && joinPoint.getSignature() != null && log.isDebugEnabled()) {
            log.debug("Starting backend service process : {}", joinPoint.getSignature().toShortString());
        }
    }

    /**
     * Adds a log for every call on a service public method.
     *
     * @param joinPoint Join point for the called method
     */
    @After("filterServicesMethods()")
    public void logServicesMethodsEnd(final JoinPoint joinPoint) {
        if (joinPoint != null && joinPoint.getSignature() != null && log.isDebugEnabled()) {
            log.debug("Ended backend service process : {}", joinPoint.getSignature().toShortString());
        }
    }
}
