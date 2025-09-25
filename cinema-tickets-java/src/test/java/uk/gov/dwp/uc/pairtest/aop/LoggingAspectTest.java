//Jesus
package uk.gov.dwp.uc.pairtest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Jesus LoggingAspectTest.java
 * <p>
 * Unit tests for {@link uk.gov.dwp.uc.pairtest.aop.LoggingAspect}. Verifies aspect logic and logging mode behavior
 * for service execution. Uses Mockito for join point and signature mocking.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
public class LoggingAspectTest {
    @Test
    void testFullModeLogsEntryAndExit() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("myTestMethod()");
        when(joinPoint.getSignature()).thenReturn(signature);
        // Simulates normal execution
        when(joinPoint.proceed()).thenReturn(null);

        LoggingAspect aspect = new LoggingAspect("FULL");
        aspect.logServiceExecution(joinPoint);
    }

    @Test
    void testExceptionIsLoggedAndRethrown() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("myErrorMethod()");
        when(joinPoint.getSignature()).thenReturn(signature);
        // Simulate exception thrown from method
        when(joinPoint.proceed()).thenThrow(new RuntimeException("simulated error"));

        LoggingAspect aspect = new LoggingAspect("FULL");
        assertThrows(RuntimeException.class, () -> aspect.logServiceExecution(joinPoint));
    }
    @Test
    void testConstructorWithValue() {
        LoggingAspect aspect = new LoggingAspect("PERF_ONLY");
        assert aspect != null;
    }

    @Test
    void testLogServiceExecution_OFF() throws Throwable {
        ProceedingJoinPoint jp = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("method()");
        when(jp.getSignature()).thenReturn(signature);

        LoggingAspect aspect = new LoggingAspect("OFF");
        // set loggingProperties.getMode() to OFF by reflection
        setLoggingPropertiesMode(aspect, "OFF");

        aspect.logServiceExecution(jp);
        verify(jp, times(1)).proceed();
    }

    @Test
    void testLogServiceExecution_PERF_ONLY() throws Throwable {
        ProceedingJoinPoint jp = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("method()");
        when(jp.getSignature()).thenReturn(signature);

        LoggingAspect aspect = new LoggingAspect("PERF_ONLY");
        setLoggingPropertiesMode(aspect, "PERF_ONLY");

        aspect.logServiceExecution(jp);
        verify(jp, times(1)).proceed();
    }

    /**
     * Reflection workaround to set private loggingProperties.mode for test coverage.
     */
    private void setLoggingPropertiesMode(LoggingAspect aspect, String mode) throws Exception {
        Field f = aspect.getClass().getDeclaredField("loggingProperties");
        f.setAccessible(true);
        f.set(aspect, new uk.gov.dwp.uc.pairtest.config.LoggingProperties() {
            @Override public String getMode() { return mode; }
        });
    }
}
