package com.debugbar.datacollector.databasequerycollector;


import com.debugbar.websocket.IWSDispatcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class JpaMethodsCollector {

    private final DataBaseQueryEntity dataBaseQueryEntity;

    private final IWSDispatcher wsDispatcher;
    public JpaMethodsCollector(final DataBaseQueryEntity dataBaseQueryEntity, final IWSDispatcher wsDispatcher){

        this.dataBaseQueryEntity = dataBaseQueryEntity;
        this.wsDispatcher = wsDispatcher;
    }

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public Object interceptJpaRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        dataBaseQueryEntity.setMethodName(methodName);
        dataBaseQueryEntity.setDispatchedFromJPA(true);
        Object result = joinPoint.proceed();
        wsDispatcher.sendCollectedDBQueries(dataBaseQueryEntity);
        return result;
    }

}
