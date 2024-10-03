    package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS = 25;
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID.");
        }

        int totalTickets = 0;
        int adultTickets = 0;
        int childTickets = 0;
        int infantTickets = 0;
        int totalCost = 0;

        // Parse and count the ticket requests
        for (TicketTypeRequest request : ticketTypeRequests) {
            int noOfTickets = request.getNoOfTickets();
            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += noOfTickets;
                    totalCost += noOfTickets * 25;
                    break;
                case CHILD:
                    childTickets += noOfTickets;
                    totalCost += noOfTickets * 15;
                    break;
                case INFANT:
                    infantTickets += noOfTickets;
                    break;
            }
            totalTickets += noOfTickets;
        }

        // Validation checks
        validateTicketPurchase(totalTickets, adultTickets, childTickets, infantTickets);

        // Proceed with payment and seat reservation
        paymentService.makePayment(accountId, totalCost);
        seatReservationService.reserveSeat(accountId, adultTickets + childTickets);
    }

    private void validateTicketPurchase(int totalTickets, int adultTickets, int childTickets, int infantTickets) {
        // Check if the total number of tickets exceeds the limit
        if (totalTickets > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets at a time.");
        }

        // Ensure that child and infant tickets are not bought without an adult
        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without at least one Adult.");
        }

        // Ensure there are enough adults for infants
        if (infantTickets > adultTickets) {
            throw new InvalidPurchaseException("Each Infant must have an accompanying Adult.");
        }
    }
}

