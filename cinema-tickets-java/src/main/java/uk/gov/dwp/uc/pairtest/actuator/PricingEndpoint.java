/*
 * Jesus Copyright (c) 2025 UK Government
 */
package uk.gov.dwp.uc.pairtest.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import uk.gov.dwp.uc.pairtest.config.TicketProperties;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jesus Read-only endpoint exposing the current ticket pricing and limits that
 * the validator uses. This helps support teams verify runtime configuration.
 *
 * <p>Usage:
 * <pre>
 *   GET /actuator/ticket-config
 * </pre>
 *
 * <p>Values are sourced from {@link TicketProperties}.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
@Endpoint(id = "ticket-config")
@RequiredArgsConstructor
public class PricingEndpoint {

    private final TicketProperties ticketProperties;

    /**
     * Returns ticket prices and maximum tickets per purchase.
     *
     * @return map with prices and maxTickets
     */
    @ReadOperation
    public Map<String, Object> read() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("prices", ticketProperties.getPrices());     // e.g., {ADULT=25, CHILD=15, INFANT=0}
        out.put("maxTickets", ticketProperties.getMaxTickets());
        out.put("timestamp", Instant.now().toString());
        return out;
    }
}
