package com.debugbar.datacollector;


import com.debugbar.datacollector.databasequerycollector.DataBaseQueriesCollector;
import com.debugbar.datacollector.databasequerycollector.DataBaseQueryEntity;
import com.debugbar.datacollector.databasequerycollector.JpaMethodsCollector;
import com.debugbar.websocket.IWSDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugBarConfig {
    @Bean
    public DataBaseQueriesCollector dataBaseQueriesCollector(final IWSDispatcher wsDispatcher) {
       return new DataBaseQueriesCollector(wsDispatcher);
    }
    @Bean
    public JpaMethodsCollector jpaMethodsCollector(final DataBaseQueryEntity dataBaseQueryEntity, final IWSDispatcher wsDispatcher)
    {
        return new JpaMethodsCollector(dataBaseQueryEntity, wsDispatcher);
    }
}