/**
 * Jesus Actuator endpoint to inspect and toggle the LoggingAspect mode at runtime.
 *
 * <p>Supported modes:
 * <ul>
 *     <li>FULL – entry, exit, and execution time</li>
 *     <li>PERF_ONLY – execution time only</li>
 *     <li>OFF – disables aspect logging</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>
 *   GET  /actuator/aspect-logging
 *   POST /actuator/aspect-logging   { "mode": "PERF_ONLY" }
 * </pre>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-24
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;
import uk.gov.dwp.uc.pairtest.config.LoggingProperties;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Exposes /actuator/aspect-logging for toggling AOP logging mode at runtime.
 * Use GET to read and POST to set the LoggingAspect mode (FULL, PERF_ONLY, or OFF).
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
@Endpoint(id = "aspect-logging")
@RequiredArgsConstructor
public class LoggingAspectEndpoint {

    private final LoggingProperties loggingProperties;

    /**
     * Returns the current LoggingAspect mode and system timestamp.
     * @return map with mode and timestamp
     */
    @ReadOperation
    public Map<String, Object> read() {
        Map<String, Object> out = new HashMap<>();
        out.put("mode", loggingProperties.getMode());
        out.put("timestamp", Instant.now().toString());
        return out;
    }

    /**
     * Updates the LoggingAspect mode (FULL | PERF_ONLY | OFF) at runtime.
     * @param mode new desired mode
     * @return map with effective mode and system timestamp
     */
    @WriteOperation
    public Map<String, Object> write(String mode) {
        String normalized = (mode == null ? "OFF" : mode.trim().toUpperCase());
        loggingProperties.setMode(normalized); // aspect reads this on each call
        Map<String, Object> out = new HashMap<>();
        out.put("newMode", loggingProperties.getMode());
        out.put("timestamp", Instant.now().toString());
        return out;
    }
}
