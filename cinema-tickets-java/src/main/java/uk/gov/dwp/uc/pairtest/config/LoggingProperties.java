/**
 * Jesus LoggingProperties.java
 * <p>
 * Configuration holder for logging/aspect toggles. Can be set via Spring Boot's application properties
 * using the prefix "logging.aspect". Allows feature control of full vs performance-only vs disabled aspect logging.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Exposes logging mode property for Spring's aspect configuration.
 * <ul>
 *   <li>Mode can be FULL, PERF_ONLY or OFF</li>
 *   <li>Configurable via property: <b>logging.aspect.mode</b></li>
 * </ul>
 *
 * <p>
 * Example in application.properties:
 * <pre>
 * logging.aspect.mode=PERF_ONLY
 * </pre>
 * </p>
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
@ConfigurationProperties(prefix = "logging.aspect")
@Getter
@Setter
public class LoggingProperties {
    /**
     * Logging mode for aspects: FULL, PERF_ONLY, or OFF
     */
    private String mode;

    /**
     * Gets the logging mode. Returns "FULL" if not set (default).
     * @return mode (never null)
     */
    public String getMode() {
        return mode == null ? "FULL" : mode;
    }
}
