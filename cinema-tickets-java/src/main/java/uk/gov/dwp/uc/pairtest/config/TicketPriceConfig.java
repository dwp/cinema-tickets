/**
 * Jesus TicketPriceConfig.java
 *
 * Spring configuration class providing beans for external ticket payment and seat reservation services
 * used in the Cinema Tickets application. This class wires up third-party payment and seat booking gateways.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;

/**
 * Creates beans for payment processing and seat reservation.
 * Bean names and types match the external gateway implementations.
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Configuration
public class TicketPriceConfig {
    /**
     * Bean for external ticket payment gateway.
     * @return new TicketPaymentServiceImpl instance
     */
    @Bean
    public TicketPaymentService ticketPaymentService() {
        return new TicketPaymentServiceImpl();
    }

    /**
     * Bean for external seat reservation gateway integration.
     * @return new SeatReservationServiceImpl instance
     */
    @Bean
    public SeatReservationService seatReservationService() {
        return new SeatReservationServiceImpl();
    }
}
