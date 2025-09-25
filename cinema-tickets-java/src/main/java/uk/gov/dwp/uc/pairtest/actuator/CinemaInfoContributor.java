/**
 * Jesus CinemaInfoContributor.java
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

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Adds application and maintainer metadata to actuator info endpoint for traceability/support.
 * Update or extend to surface additional diagnostics or build metadata as needed.
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
public class CinemaInfoContributor implements InfoContributor {

    /**
     * Contributes extra info about the application to the /actuator/info endpoint.
     * @param builder the Info.Builder object from Spring Boot
     */
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("application",
                Map.of("name", "Cinema Ticket Service",
                       "description", "Manages ticket purchases with payment and seat reservation",
                       "version", "1.0.0"))
               .withDetail("maintainers",
                Map.of("team", "UC Pair Test DevOps",
                       "contact", "support@yourcompany.com"));
    }
}
