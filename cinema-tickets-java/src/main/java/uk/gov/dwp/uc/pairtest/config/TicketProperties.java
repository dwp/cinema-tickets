/**
 * Jesus TicketProperties.java
 *
 * Spring Boot configuration binding for ticket-related properties:
 * maximum tickets per purchase and per-type pricing. Used throughout the application
 * to enforce business constraints and price calculations based on config.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;

/**
 * Binds ticket configuration properties to Spring beans:
 * <ul>
 *   <li><b>maxTickets</b>: Maximum allowed per purchase</li>
 *   <li><b>prices</b>: Ticket type-price mapping (e.g., ADULT=25, CHILD=15, INFANT=0)</li>
 * </ul>
 * Configurable via <code>application.properties</code> using the prefix "ticket".
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@ConfigurationProperties(prefix = "ticket")
public class TicketProperties {
    private int maxTickets;
    private Map<String, Integer> prices;

    /**
     * @return maximum number of tickets allowed per purchase
     */
    public int getMaxTickets() {
        return maxTickets;
    }

    /**
     * Sets the maximum allowed ticket quantity per purchase.
     * @param maxTickets configured limit
     */
    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }

    /**
     * @return the price map keyed by ticket type name (ADULT, CHILD, etc)
     */
    public Map<String, Integer> getPrices() {
        return prices;
    }

    /**
     * Sets the type-to-price map for tickets.
     * @param prices new ticket prices map
     */
    public void setPrices(Map<String, Integer> prices) {
        this.prices = prices;
    }
}
