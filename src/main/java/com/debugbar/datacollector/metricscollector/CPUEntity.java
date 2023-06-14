package com.debugbar.datacollector.metricscollector;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CPUEntity {
    long processCpuTime;
    double systemCpuLoad;
    double processCpuLoad;
}
