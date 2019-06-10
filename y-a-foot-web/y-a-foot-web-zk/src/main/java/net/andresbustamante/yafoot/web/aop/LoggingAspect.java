package net.andresbustamante.yafoot.web.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging des classes de l'application Web
 */
@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* net.andresbustamante.yafoot.web.services.*UIService.*(..))")
    public void filtrerServicesUI() {
        // no-op
    }

    @Before("filtrerServicesUI()")
    public void loggerAppelsUI(JoinPoint joinPoint) {
        if (joinPoint != null && joinPoint.getSignature() != null) {
            log.info("Service UI actuel : {}", joinPoint.getSignature().toShortString());
        }
    }
}
