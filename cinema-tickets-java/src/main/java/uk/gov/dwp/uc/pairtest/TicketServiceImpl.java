package uk.gov.dwp.uc.pairtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * <p>
 * Default implementation of the {@link TicketService} interface.
 * </p>
 * <p>
 * This service is responsible for following actions:
 * <ul>
 *     <li>Handles requests for purchasing tickets.</li>
 *     <li>Validates purchase requests against certain business rules.</li>
 *     <li>Calls payment gateway and seat reservation.</li>
 * </ul>
 * </p>
 *
 * @since 1.0.0
 *
 * @author Hansaka Weerasingha
 *
 * @version $Id$
 */
public class TicketServiceImpl implements TicketService {

    private final static Logger          LOGGER             = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final static int             MAX_TICKET_COUNT   = 20;
    private final TicketPaymentService   paymentService     = new TicketPaymentServiceImpl();
    private final SeatReservationService reservationService = new SeatReservationServiceImpl();

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(final Long accountId, final TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (!validatePurchaseRequest(accountId, ticketTypeRequests)) {
            LOGGER.error("Invalid ticket type purchase request!");
            throw new InvalidPurchaseException("Ticket purchase request is invalid. Please verify all the parameters and submit again!");
        }

        try {
            final double totalAmount = calTotalAmount(ticketTypeRequests);
            paymentService.makePayment(accountId, (int) totalAmount);
            LOGGER.debug("Call to payment service was successful!");

            final int totalSeats = calTotalSeats(ticketTypeRequests);
            reservationService.reserveSeat(accountId, totalSeats);
            LOGGER.debug("Call to reservation service was successful!");
        } catch (final Throwable e) {
            LOGGER.error("Error occurred while calling third party services!", e);
        }
        
        LOGGER.info("Ticket type purchase request processed successfully!");
    }

    /**
     * Validates the purchase request against certain business rules.
     *
     * @param  accountId          The user account Id
     * @param  ticketTypeRequests Ticket purchase requests
     *
     * @return                    True if the purchase request is valid else False
     */
    private boolean validatePurchaseRequest(final Long accountId, final TicketTypeRequest... ticketTypeRequests) {
        // Check if account is valid
        if (accountId <= 0) {
            return false;
        }

        // Check if ticket type requests are available
        if (ticketTypeRequests == null) {
            return false;
        } else {
            int totTickets = 0;
            boolean hasAdultTicket = false;

            for (final TicketTypeRequest request : ticketTypeRequests) {
                if (request != null) {
                    final int noOfTickets = request.getNoOfTickets();
                    totTickets += noOfTickets;
                    if (request.getTicketType().isAdult()) {
                        hasAdultTicket = true;
                    }
                }
            }

            // Check if max ticket count has exceeded
            if (totTickets > MAX_TICKET_COUNT) {
                return false;
            }

            // Check if adult ticket is available
            if (!hasAdultTicket) {
                return false;
            }
        }

        // All rules met. Request is valid.
        return true;
    }

    /**
     * Calculates the total amount to be debited from the account Id.
     *
     * @param  ticketTypeRequests The ticket purchase requests
     *
     * @return                    The total amount to be debited
     */
    private double calTotalAmount(final TicketTypeRequest... ticketTypeRequests) {
        double totAmount = 0;

        for (final TicketTypeRequest request : ticketTypeRequests) {
            final int noOfTickets = request.getNoOfTickets();
            final double ticketPrice = request.getTicketType().getPrice();

            totAmount += ticketPrice * noOfTickets;
        }

        return totAmount;
    }

    /**
     * Calculates the total seats to be reserved on behalf of the account Id.
     *
     * @param  ticketTypeRequests The ticket purchase requests
     *
     * @return                    The total seats to be reserved
     */
    private int calTotalSeats(final TicketTypeRequest... ticketTypeRequests) {
        int totSeats = 0;

        for (final TicketTypeRequest request : ticketTypeRequests) {
            final int noOfTickets = request.getNoOfTickets();

            if (request.getTicketType().isSeatRequired()) {
                totSeats += noOfTickets;
            }
        }

        return totSeats;
    }

}
