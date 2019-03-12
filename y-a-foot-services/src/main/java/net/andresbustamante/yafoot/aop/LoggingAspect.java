package net.andresbustamante.yafoot.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Log log = LogFactory.getLog(LoggingAspect.class);

    @Pointcut("execution(* net.andresbustamante.yafoot.services.impl.*ServiceImpl.*(..))")
    public void filtrerServicesMetier() {
    }

    @Before("filtrerServicesMetier()")
    public void loggingMethodesMetier(JoinPoint joinPoint) throws Throwable {
        log.info("Service actuel : " + joinPoint.getSignature().toShortString());
    }
}
