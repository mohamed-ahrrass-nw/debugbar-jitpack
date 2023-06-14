package com.debugbar.websocket;

import com.debugbar.datacollector.databasequerycollector.DataBaseQueryEntity;
import com.debugbar.datacollector.exceptionscollector.ExceptionsCollectorEntity;
import com.debugbar.datacollector.metricscollector.CPUEntity;
import com.debugbar.datacollector.metricscollector.HeapMemoryEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WSDispatcher implements IWSDispatcher {
    private final SimpMessagingTemplate template;

    private static final String BASE_PATH = "/collectors";

    private static final String DB_QUERY_PATH = "/db-queries";
    private static final String EXCEPTIONS_PATH = "/exceptions";
    private static final String HEAP_MEMORY_PATH = "/metrics/heap";
    private static final String CPU_PATH = "/metrics/cpu";

    public WSDispatcher(final SimpMessagingTemplate template)
    {
        this.template = template;
    }
    @Override
    public void sendCollectedDBQueries(final DataBaseQueryEntity dataBaseQueryEntity){
        template.convertAndSend(BASE_PATH + DB_QUERY_PATH, dataBaseQueryEntity);
    }

    @Override
    public void sendCollectedExceptions(final ExceptionsCollectorEntity exceptionsCollector) {
        template.convertAndSend(BASE_PATH + EXCEPTIONS_PATH, exceptionsCollector);
    }

    @Override
    public void sendCollectedMemoryHeap(HeapMemoryEntity heapMemoryEntity) {
        template.convertAndSend(BASE_PATH + HEAP_MEMORY_PATH, heapMemoryEntity);
    }

    @Override
    public void sendCollectedCPUUsage(CPUEntity cpuEntity) {
        template.convertAndSend(BASE_PATH + CPU_PATH, cpuEntity);
    }
}
