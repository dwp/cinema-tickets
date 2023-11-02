package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;


public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS_ALLOWED = 20;
    private static final int ADULT_TICKET_PRICE = 20;
    private static final int CHILD_TICKET_PRICE = 10;

    private final TicketPaymentService paymentService;
    private final SeatReservationService reservationService;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Account ID is not valid.");
        }

        int totalTickets = 0;
        int adultTickets = 0;
        int childTickets = 0;
        int totalAmount = 0;
        int totalSeatsToReserve = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            totalTickets += request.getNoOfTickets();

            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += request.getNoOfTickets();
                    totalAmount += request.getNoOfTickets() * ADULT_TICKET_PRICE;
                    totalSeatsToReserve += request.getNoOfTickets();
                    break;
                case CHILD:
                    childTickets += request.getNoOfTickets();
                    totalAmount += request.getNoOfTickets() * CHILD_TICKET_PRICE;
                    totalSeatsToReserve += request.getNoOfTickets();
                    break;
                case INFANT:
                    break;
            }
        }

        if (totalTickets > MAX_TICKETS_ALLOWED) {
            throw new InvalidPurchaseException("Cannot purchase more than 20 tickets at a time.");
        }

        if (adultTickets == 0 && (childTickets > 0 || totalTickets > adultTickets)) {
            throw new InvalidPurchaseException("Child and Infant tickets require at least one Adult ticket.");
        }

        paymentService.makePayment(accountId, totalAmount);
        reservationService.reserveSeat(accountId, totalSeatsToReserve);
    }

}
