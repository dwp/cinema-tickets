 /* Jesus CustomFeatureHealthIndicator.java
 *
 * Custom Spring Boot InfoContributor for the Cinema Ticket Service.
 * Adds descriptive metadata to the /actuator/info endpoint for operational visibility.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.actuator;

import org.springframework.boot.actuate.health.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Health indicator that contributes a custom detail to the Spring Boot /actuator/health endpoint
 * only when the monitoring feature toggle is enabled.
 * <p>
 * When <b>feature.monitoring.enabled=true</b> in application properties, this bean adds a
 * "featureMonitoring: enabled" detail to the overall health status, for operational diagnostics
 * and feature visibility in distributed systems.
 * </p>
 *
 * <ul>
 *   <li>The health status is always reported as "UP" if the bean is active</li>
 *   <li>This bean is <b>conditionally registered</b> only when monitoring is enabled</li>
 *   <li>Bean name: "customFeatureHealth" (can be referenced in custom health groups)</li>
 * </ul>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component("customFeatureHealth")
@ConditionalOnProperty(name = "feature.monitoring.enabled", havingValue = "true")
public class CustomFeatureHealthIndicator implements HealthIndicator {

    /**
     * Returns a health status of UP with a custom detail when monitoring is enabled.
     * Key: "featureMonitoring" → Value: "enabled".
     * @return Health status and details for the /actuator/health endpoint
     */
    @Override
    public Health health() {
        return Health.up()
            .withDetail("featureMonitoring", "enabled")
            .build();
    }
}
