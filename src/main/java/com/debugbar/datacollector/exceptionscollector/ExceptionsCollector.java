package com.debugbar.datacollector.exceptionscollector;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionsCollector {

    private final IExceptionsCollectorHelper exceptionsCollectorHelper;

    public ExceptionsCollector(final IExceptionsCollectorHelper exceptionsCollectorHelper) {
        this.exceptionsCollectorHelper = exceptionsCollectorHelper;
    }

    @Before("@within(org.springframework.web.bind.annotation.RestControllerAdvice) " +
            "|| @within(org.springframework.web.bind.annotation.ControllerAdvice)")
    public void beforeControllerAdviceMethodExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args)
            if(arg instanceof Exception)
                exceptionsCollectorHelper.processException((Exception) arg);
    }
}
