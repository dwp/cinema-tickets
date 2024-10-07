package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import java.util.Arrays;

/**
 * Implementation of the TicketService interface for managing the purchase of cinema tickets.
 * This class handles ticket purchase requests, validates them, calculates total costs,
 * and interacts with payment and seat reservation services.
 */
public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS = 25; // Maximum number of tickets that can be purchased in one transaction.
    private static final int ADULT_TICKET_PRICE = 25; // Price of an adult ticket.
    private static final int CHILD_TICKET_PRICE = 15; // Price of a child ticket.

    private final TicketPaymentService paymentService; // Service to handle ticket payments.
    private final SeatReservationService seatReservationService; // Service to reserve seats.

    /**
     * Constructs a new TicketServiceImpl instance.
     *
     * @param paymentService         the service to process payments.
     * @param seatReservationService  the service to reserve seats.
     */
    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    /**
     * Purchases tickets for a given account.
     *
     * @param accountId             the ID of the account making the purchase.
     * @param ticketTypeRequests    an array of ticket type requests.
     * @throws InvalidPurchaseException if the purchase request is invalid.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validatePurchaseRequest(accountId, ticketTypeRequests); // Validate the purchase request.

        int totalSeats = calculateTotalSeats(ticketTypeRequests); // Calculate total seats needed.
        int totalAmount = calculateTotalAmount(ticketTypeRequests); // Calculate total amount to be paid.

        paymentService.makePayment(accountId, totalAmount); // Process payment.
        seatReservationService.reserveSeat(accountId, totalSeats); // Reserve seats.
    }

    /**
     * Validates the purchase request for tickets.
     *
     * @param accountId             the ID of the account making the purchase.
     * @param ticketTypeRequests    an array of ticket type requests.
     * @throws InvalidPurchaseException if the request contains invalid data.
     */
    private void validatePurchaseRequest(Long accountId, TicketTypeRequest[] ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID."); // Check for valid account ID.
        }

        long adultTickets = 0;
        long childTickets = 0;
        long infantTickets = 0;

        // Count the number of each ticket type.
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT -> adultTickets += request.getNoOfTickets();
                case CHILD -> childTickets += request.getNoOfTickets();
                case INFANT -> infantTickets += request.getNoOfTickets();
            }
        }

        long totalTickets = adultTickets + childTickets + infantTickets;

        if (totalTickets == 0) {
            throw new InvalidPurchaseException("At least one ticket must be purchased.");
        }

        if (totalTickets > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than " + MAX_TICKETS + " tickets.");
        }

        if ((childTickets > 0 || infantTickets > 0) && adultTickets == 0) {
            throw new InvalidPurchaseException("Child or infant tickets cannot be purchased without an adult ticket.");
        }
    }

    /**
     * Calculates the total amount to be paid for the ticket requests.
     *
     * @param ticketTypeRequests    an array of ticket type requests.
     * @return the total amount to be paid.
     */
    private int calculateTotalAmount(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .mapToInt(request -> getTicketPrice(request.getTicketType()) * request.getNoOfTickets())
                .sum(); // Calculate total amount by summing the price for each ticket type.
    }

    /**
     * Gets the price of a ticket based on its type.
     *
     * @param ticketType the type of the ticket.
     * @return the price of the ticket.
     */
    private int getTicketPrice(TicketTypeRequest.Type ticketType) {
        return switch (ticketType) {
            case ADULT -> ADULT_TICKET_PRICE;
            case CHILD -> CHILD_TICKET_PRICE;
            case INFANT -> 0; // Infants are free.
        };
    }

    /**
     * Calculates the total number of seats required for the ticket requests.
     *
     * @param ticketTypeRequests    an array of ticket type requests.
     * @return the total number of seats required.
     */
    private int calculateTotalSeats(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .filter(request -> request.getTicketType() == TicketTypeRequest.Type.ADULT
                        || request.getTicketType() == TicketTypeRequest.Type.CHILD) // Only adults and children require seats.
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum(); // Sum the number of tickets for adults and children.
    }
}
