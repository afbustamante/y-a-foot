package net.andresbustamante.yafoot.commons.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* net.andresbustamante.yafoot.*.services.impl.*ServiceImpl.*(..))")
    public void filterServicesMethods() {
        // no-op
    }

    @Before("filterServicesMethods()")
    public void logServicesMethods(JoinPoint joinPoint) {
        if (joinPoint != null && joinPoint.getSignature() != null && log.isDebugEnabled()) {
            log.debug("Active backend service : {}", joinPoint.getSignature().toShortString());
        }
    }
}
