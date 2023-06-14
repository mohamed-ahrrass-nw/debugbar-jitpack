package com.debugbar.datacollector.exceptionscollector;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
class ExceptionsAspectUnitTests {
    @InjectMocks
    private ExceptionsCollector exceptionsCollector;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private IExceptionsCollectorHelper exceptionsCollectorHelper;


    @Test
    void testHandleServiceExceptions() {
        Exception testException = new Exception();
        when(joinPoint.getArgs()).thenReturn(new Object[]{testException});

        // Given
        doNothing().when(exceptionsCollectorHelper).processException(testException);

        exceptionsCollector.beforeControllerAdviceMethodExecution(joinPoint);
        // Then
        verify(exceptionsCollectorHelper).processException(testException);
    }
}