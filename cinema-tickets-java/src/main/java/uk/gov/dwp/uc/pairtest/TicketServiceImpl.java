package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    TicketServiceImpl(TicketPaymentService ticketPaymentService,
                      SeatReservationService seatReservationService) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {
        // Check if the account id is valid
        if (accountId < 1) throw new InvalidPurchaseException("Invalid Account ID.");

        // Validate the ticket type requests.
        validateTicketTypeRequests(ticketTypeRequests);

        // Calculate the total amount to pay.
        int totalAmountToPay = calculateTotalAmountToPay(ticketTypeRequests);

        // Make the payment.
        ticketPaymentService.makePayment(accountId, totalAmountToPay);

        // Reserve the seats.
        int totalSeatsToAllocate = calculateTotalSeatsToAllocate(ticketTypeRequests);
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);
    }

    private void validateTicketTypeRequests(TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {
        // Check if TicketTypeRequest is not present or null
        if(ticketTypeRequests == null || ticketTypeRequests.length < 1)
            throw new InvalidPurchaseException("No ticket request made.");

        // Check if each TicketTypeRequest is valid and total tickets are less than 20
        validateEachTicketTypeRequestAndMaxTicketsCap(ticketTypeRequests);

        // Check if the ticket purchaser is trying to purchase child or infant tickets
        // without purchasing an adult ticket.
        validateMinorGuardianship(ticketTypeRequests);

        // Check if there are more infant tickets requested than adult tickets
        validateInfantToAdultTicketsRatio(ticketTypeRequests);
    }

    private void validateMinorGuardianship(TicketTypeRequest... ticketTypeRequests){
        boolean hasAdultTicket = false;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests)
            if (ticketTypeRequest.getTicketType() == ADULT) {
                hasAdultTicket = true;
                break;
            }

        if (!hasAdultTicket && (containsChildTicket(ticketTypeRequests)
                || containsInfantTicket(ticketTypeRequests))) {
            throw new InvalidPurchaseException("Cannot purchase child or infant tickets without purchasing " +
                    "an adult ticket.");
        }
    }

    private boolean containsChildTicket(TicketTypeRequest... ticketTypeRequests) {
        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests)
            if (ticketTypeRequest.getTicketType() == CHILD) return true;

        return false;
    }

    private boolean containsInfantTicket(TicketTypeRequest... ticketTypeRequests) {
        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests)
            if (ticketTypeRequest.getTicketType() == INFANT) return true;

        return false;
    }

    private int calculateTotalAmountToPay(TicketTypeRequest... ticketTypeRequests) {
        int totalAmountToPay = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            int ticketPrice = getTicketPrice(ticketTypeRequest.getTicketType());
            totalAmountToPay += ticketPrice * ticketTypeRequest.getNoOfTickets();
        }

        return totalAmountToPay;
    }

    private int getTicketPrice(TicketTypeRequest.Type type) {
        return switch (type) {
            case ADULT -> 20;
            case CHILD -> 10;
            case INFANT -> 0;
        };
    }

    private int calculateTotalSeatsToAllocate(TicketTypeRequest... ticketTypeRequests) {
        int totalSeatsToAllocate = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests)
            if (ticketTypeRequest.getTicketType() != INFANT)
                totalSeatsToAllocate += ticketTypeRequest.getNoOfTickets();

        return totalSeatsToAllocate;
    }

    private void validateInfantToAdultTicketsRatio(TicketTypeRequest... ticketTypeRequests) {
        int adultTickets = 0;
        int infantTickets = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests)
            if (ticketTypeRequest.getTicketType() == INFANT)
                infantTickets += ticketTypeRequest.getNoOfTickets();
            else if (ticketTypeRequest.getTicketType() == ADULT)
                adultTickets += ticketTypeRequest.getNoOfTickets();

        if(infantTickets > adultTickets)
            throw new InvalidPurchaseException("More infants tickets than adult tickets.");
    }

    private void validateEachTicketTypeRequestAndMaxTicketsCap(TicketTypeRequest... ticketTypeRequests){
        int totalTickets = 0;

        for(TicketTypeRequest ticketTypeRequest : ticketTypeRequests){
            // Check if the ticket type request is present
            if (ticketTypeRequest == null)
                throw new InvalidPurchaseException("Ticket type request is not valid.");
                // Check if ticket type is present
            else if (ticketTypeRequest.getTicketType() == null)
                throw new InvalidPurchaseException("Ticket type is null.");
                // Check if number of tickets are not positive
            else if (ticketTypeRequest.getNoOfTickets() < 1)
                throw new InvalidPurchaseException("Number of tickets to be purchased must be positive.");

            totalTickets += ticketTypeRequest.getNoOfTickets();

            // Check if the number of tickets requested is greater than the maximum allowed.
            if (totalTickets > 20)
                throw new InvalidPurchaseException("Cannot purchase more than 20 tickets at a time.");
        }
    }
}