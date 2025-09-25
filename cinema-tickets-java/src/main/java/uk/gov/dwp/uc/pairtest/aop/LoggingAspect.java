/**
 * Jesus LoggingAspect.java
 * 
 * Aspect for logging entry, exit, and execution time of all service methods using Spring AOP.
 * Provides full trace logging, performance-only logging, or disables logging based on configuration.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.dwp.uc.pairtest.config.LoggingProperties;

/**
 * Spring AOP aspect for logging lifecycle of service method invocations.
 * <ul>
 *   <li>If mode is FULL – logs method entry, exit, duration.</li>
 *   <li>If mode is PERF_ONLY – logs only the performance stats (duration).</li>
 *   <li>If mode is OFF – disables all AOP logging.</li>
 * </ul>
 * Mode is configurable using logging.aspect.mode property (see LoggingProperties).
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "feature.logging.enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAspect {

    private static final String PERF_ONLY = "PERF_ONLY";
    private static final String FULL = "FULL";
    private static final String OFF = "OFF";
    private String mode;
    private LoggingProperties loggingProperties;

    /**
     * Default constructor with FULL logging.
     */
    public LoggingAspect() {
        this.mode = "FULL";
        this.loggingProperties = new LoggingProperties();
    }

    /**
     * Constructor with explicit logging mode (typically set by Spring from configuration).
     * @param mode Logging mode (FULL, PERF_ONLY, or OFF)
     */
    public LoggingAspect(@Value("${logging.aspect.mode:FULL}") String mode) {
        this.mode = mode.toUpperCase();
        this.loggingProperties = new LoggingProperties();
    }

    /**
     * Intercepts all public methods in service layer for logging/tracing.
     * Logs entry, exit, performance, or exception according to mode.
     * @param joinPoint intercepted joinpoint from Spring AOP
     * @return method result
     * @throws Throwable if the service method throws
     */
    @Around("execution(* uk.gov.dwp.uc.pairtest.service..*(..))")
    public Object logServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String mode = loggingProperties.getMode().toUpperCase();

        if ("OFF".equals(mode)) {
            return joinPoint.proceed(); // logging disabled
        }

        long start = System.currentTimeMillis();

        if ("FULL".equals(mode)) {
            log.info("➡️ Entering method: {}", methodName);
        }

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            if ("PERF_ONLY".equals(mode)) {
                log.debug("⏱ Method {} executed in {} ms", methodName, duration);
            } else if ("FULL".equals(mode)) {
                log.info("✅ Exiting method: {} (took {} ms)", methodName, duration);
            }

            return result;
        } catch (Exception ex) {
            log.error("❌ Exception in method {}: {}", methodName, ex.getMessage(), ex);
            throw ex;
        }
    }
}
