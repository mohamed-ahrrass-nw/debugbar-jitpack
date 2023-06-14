package com.debugbar.datacollector.exceptionscollector;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionsCollectorEntity {
    private final String simpleName;
    private final String message;
    private final String stackTrace;
    private final String packageName;

    private final String requestID;

    private final String fileName;
    private final String className;
    private final String methodName;
    private final int line;
}
