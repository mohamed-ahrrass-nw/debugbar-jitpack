package com.debugbar.websocket;


import com.debugbar.datacollector.databasequerycollector.DataBaseQueryEntity;
import com.debugbar.datacollector.exceptionscollector.ExceptionsCollectorEntity;
import com.debugbar.datacollector.metricscollector.CPUEntity;
import com.debugbar.datacollector.metricscollector.HeapMemoryEntity;

public interface IWSDispatcher {
    void sendCollectedDBQueries(DataBaseQueryEntity dataBaseQueryEntity);
    void sendCollectedExceptions(ExceptionsCollectorEntity exceptionsCollector);
    void sendCollectedMemoryHeap(HeapMemoryEntity heapMemoryEntity);
    void sendCollectedCPUUsage(CPUEntity cpuEntity);
}
