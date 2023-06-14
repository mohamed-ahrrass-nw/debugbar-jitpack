package com.debugbar.datacollector.metricscollector;

import com.debugbar.websocket.IWSDispatcher;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MetricsCollector implements IMetricsCollector{

    private final float UNCERTAINTY = 0.1f; // 10 percent of change
    private final int GB_TO_BYTES = 1_048_576;

    private final MemoryMXBean memoryMXBean;
    private final ScheduledExecutorService executorService;
    private final IWSDispatcher collectorWebSocketService;
    private HeapMemoryEntity heapMemoryEntity = new HeapMemoryEntity(0, 0, 0, 0, LocalTime.now());

    public MetricsCollector(IWSDispatcher collectorWebSocketService) {
        this.collectorWebSocketService = collectorWebSocketService;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::getHeapMemoryUsage, 0, 5, TimeUnit.SECONDS);
    }

    public boolean isToSendMemoryMeasure(double newMeasure) {

        double previousMeasure = heapMemoryEntity.getUsedMemory();
        double upperEdge = previousMeasure + previousMeasure * UNCERTAINTY;
        double lowerEdge = previousMeasure - previousMeasure * UNCERTAINTY;

        return !(lowerEdge <= newMeasure && newMeasure <= upperEdge);
    }

    @Override
    public void getHeapMemoryUsage() {
        double usedMemory = (double)this.memoryMXBean.getHeapMemoryUsage().getUsed() / GB_TO_BYTES;

        if(this.isToSendMemoryMeasure(usedMemory)) {
            double maxMemory = (double)this.memoryMXBean.getHeapMemoryUsage().getMax() / GB_TO_BYTES;
            double committedMemory = (double)this.memoryMXBean.getHeapMemoryUsage().getCommitted() / GB_TO_BYTES;
            double initialMemory = (double)this.memoryMXBean.getHeapMemoryUsage().getInit() / GB_TO_BYTES;
            LocalTime timestamp = LocalTime.now();

            heapMemoryEntity.setMaxHeapMemory(maxMemory);
            heapMemoryEntity.setUsedMemory(usedMemory);
            heapMemoryEntity.setCommittedMemory(committedMemory);
            heapMemoryEntity.setInitialMemory(initialMemory);
            heapMemoryEntity.setTimestamp(timestamp);

            collectorWebSocketService.sendCollectedMemoryHeap(heapMemoryEntity);
        }
    }
}
