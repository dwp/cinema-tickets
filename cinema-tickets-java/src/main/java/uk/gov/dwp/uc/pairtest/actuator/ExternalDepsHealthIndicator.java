/**
 * Jesus ExternalDepsHealthIndicator.java
 *
 * Health indicator that reflects the application's ability to wire external service beans
 * such as payment gateway and seat reservation in the Cinema Ticket Service.
 * Exposed under /actuator/health and useful to verify connectivity to third-party gateways
 * at the bean-wiring level.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-24
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

/**
 * Checks the presence of essential external service beans as an operational health signal.
 * Extend for active network checks as needed.
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
@RequiredArgsConstructor
public class ExternalDepsHealthIndicator implements HealthIndicator {

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    /**
     * Reports UP if both external service beans are present; DOWN otherwise.
     * @return Health status for third-party dependencies
     */
    @Override
    public Health health() {
        boolean paymentAvailable = paymentService != null;
        boolean seatAvailable = seatReservationService != null;

        if (paymentAvailable && seatAvailable) {
            return Health.up()
                    .withDetail("paymentService", "available")
                    .withDetail("seatReservationService", "available")
                    .build();
        }
        return Health.down()
                .withDetail("paymentService", paymentAvailable ? "available" : "missing")
                .withDetail("seatReservationService", seatAvailable ? "available" : "missing")
                .build();
    }
}
