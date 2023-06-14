package com.debugbar.datacollector.databasequerycollector;

import com.debugbar.websocket.IWSDispatcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class DataBaseQueriesCollector {

    @Autowired
    private DataBaseQueryEntity dataBaseQueryEntity;
    final private IWSDispatcher wsDispatcher;
    public DataBaseQueriesCollector(final IWSDispatcher wsDispatcher)
    {
        this.wsDispatcher = wsDispatcher;
    }

    private static final String API_POINTCUT = "execution(* javax.sql.DataSource.getConnection(..))";
    @Pointcut(API_POINTCUT)
    public void apiPointCut(){}

    @Around("apiPointCut()")
    public Object intercept(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Connection connection = (Connection) joinPoint.proceed();
        return Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    final String methodName = method.getName();
                    if ("prepareStatement".equals(methodName) || "prepareCall".equals(methodName)) {
                        final LocalDateTime timeStamp = LocalDateTime.now();
                        final long startTime = System.nanoTime();
                        final PreparedStatement preparedStatement = (PreparedStatement) method.invoke(connection, args);
                        final long endTime = System.nanoTime();
                        final long executionDuration = endTime - startTime;

                        List<String> boundParams = new ArrayList<>();
                        List<String> paramsDbTypes = new ArrayList<>();
                        List<String> paramsJavaTypes = new ArrayList<>();
                        final PreparedStatement proxyStatement = (PreparedStatement) Proxy.newProxyInstance(
                                preparedStatement.getClass().getClassLoader(),
                                preparedStatement.getClass().getInterfaces(),
                                (stmtProxy, stmtMethod, stmtArgs) -> {
                                    ParameterMetaData paramMetaData = preparedStatement.getParameterMetaData();
                                    if (stmtMethod.getName().startsWith("set")) {
                                        DataBaseQueriesEntityHelper.AssignTypesToSqlValues(stmtArgs, paramMetaData, boundParams, paramsDbTypes, paramsJavaTypes);
                                    }
                                    return stmtMethod.invoke(preparedStatement, stmtArgs);
                                }
                        );
                        final DataBaseQueryEntity jdbcData =
                                DataBaseQueriesEntityHelper
                                .build(
                                        args,
                                        methodName,
                                        executionDuration,
                                        timeStamp,
                                        boundParams,
                                        paramsDbTypes,
                                        paramsJavaTypes,
                                        dataBaseQueryEntity.isDispatchedFromJPA()
                                    );
                        dataBaseQueryEntity.mapper(jdbcData);
                        if(!dataBaseQueryEntity.isDispatchedFromJPA())
                        {
                            wsDispatcher.sendCollectedDBQueries(dataBaseQueryEntity);
                        }
                        return proxyStatement;
                    }
                    return method.invoke(connection, args);
                }
        );
    }
}
