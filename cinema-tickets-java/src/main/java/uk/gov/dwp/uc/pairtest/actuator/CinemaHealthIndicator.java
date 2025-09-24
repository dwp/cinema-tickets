/**
 * Jesus CinemaHealthIndicator.java
 *
 * Custom Spring Boot health indicator for the Cinema Ticket Service application.
 * Contributes status and application information to the /actuator/health endpoint for monitoring.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Provides custom health status and version details for actuator health endpoint.
 * Extend or enhance this indicator to add additional checks (such as DB, external services, etc.)
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
public class CinemaHealthIndicator implements HealthIndicator {

    /**
     * Returns health status and service info for /actuator/health
     * @return Health.UP if service is running, Health.DOWN otherwise
     */
    @Override
    public Health health() {
        // You can enhance this check (DB connection, Redis availability, etc.)
        boolean serviceUp = true;

        if (serviceUp) {
            return Health.up()
                    .withDetail("service", "Cinema Ticket Service is running")
                    .withDetail("version", "1.0.0")
                    .build();
        } else {
            return Health.down()
                    .withDetail("service", "Cinema Ticket Service is unavailable")
                    .build();
        }
    }
}
