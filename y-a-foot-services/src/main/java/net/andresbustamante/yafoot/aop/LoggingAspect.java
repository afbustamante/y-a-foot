package net.andresbustamante.yafoot.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* net.andresbustamante.yafoot.services.impl.*ServiceImpl.*(..))")
    public void filtrerServicesMetier() {
    }

    @Around("filtrerServicesMetier()")
    public void loggingMethodesMetier(ProceedingJoinPoint joinPoint) throws Throwable {
        Long heureDebut = System.currentTimeMillis();
        Log log = LogFactory.getLog(joinPoint.getTarget().getClass());

        try {
            joinPoint.proceed();
        } finally {
            long duree = System.currentTimeMillis() - heureDebut;
            log.info("Service actuel : " + joinPoint.getSignature().toShortString() + " Durée : " + String.valueOf(duree) + "ms");
        }
    }
}
