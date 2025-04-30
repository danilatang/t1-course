package org.example.t1coursetask1.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(org.example.t1coursetask1.aspect.annotation.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Выполнение метода: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(org.example.t1coursetask1.aspect.annotation.Loggable)", returning = "result")
    public void logAfterReturning(Object result) {
        logger.info("Метод выполнен успешно, результат: {}", result);
    }

    @AfterThrowing(pointcut = "@annotation(org.example.t1coursetask1.aspect.annotation.ExceptionHandling)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("Метод {} выбросил исключение: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }

    @Around("@annotation(org.example.t1coursetask1.aspect.annotation.Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis() - startTime;
        logger.info("Метод {} выполнялся {} мс", joinPoint.getSignature(), endTime);
        return result;
    }
}
