package uk.gov.dwp.uc.pairtest;

import java.util.Arrays;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
   
    //Maximum allowed tickets to purchase at a time
    private static final int MAX_TICKETS = 25;
	
    //Ticket price for each category
    private static final int ADULT_TICKET_PRICE = 25;
    private static final int CHILD_TICKET_PRICE = 15;
    private static final int INFANT_TICKET_PRICE = 0;
    
    private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationService;
	
    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
	this.ticketPaymentService = ticketPaymentService;
	this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
		
	if (accountId == null || accountId <= 0) {
	    throw new InvalidPurchaseException("Invalid account ID: " + accountId);
   	}
		
	validateTicketPurchaseRequest(ticketTypeRequests);

        int totalSeats = computeTotalSeats(ticketTypeRequests);
        int totalAmount = computeTotalAmount(ticketTypeRequests);

        ticketPaymentService.makePayment(accountId, totalAmount);
        seatReservationService.reserveSeat(accountId, totalSeats);
    }
	
    private void validateTicketPurchaseRequest(TicketTypeRequest... ticketTypeRequests) {
	
	int totalTickets = 0;
	
	for (TicketTypeRequest ticketRequest : ticketTypeRequests) {
		int numberOfTickets = ticketRequest.getNoOfTickets();

	// Check for negative ticket numbers
	if (numberOfTickets < 0) {
	    throw new InvalidPurchaseException("Ticket number cannot be negative for ticket type: " + ticketRequest.getTicketType());
	}
	totalTickets += numberOfTickets;
	}
	
	int adultCount = Arrays.stream(ticketTypeRequests)
		.filter(request -> request.getTicketType() == TicketTypeRequest.Type.ADULT)
		.mapToInt(TicketTypeRequest::getNoOfTickets)
		.sum();
	
	if (totalTickets > MAX_TICKETS) {
	    throw new InvalidPurchaseException("Cannot purchase more than " + MAX_TICKETS + " tickets.");
	}
	    
	/*
	 * if (adultCount == 0) { throw new
	 * InvalidPurchaseException("Child or Infant tickets cannot be purchased without an Adult ticket"); }
	 */
	// Ensure that child and infant tickets cannot be purchased without an adult ticket
        for (TicketTypeRequest ticketRequest : ticketTypeRequests) {
            if ((ticketRequest.getTicketType() == TicketTypeRequest.Type.CHILD || ticketRequest.getTicketType() == TicketTypeRequest.Type.INFANT) && adultCount == 0) {
                throw new InvalidPurchaseException("Child or Infant tickets cannot be purchased without an Adult ticket.");
            }
        }
		
    }

    private int computeTotalSeats(TicketTypeRequest... ticketTypeRequests) {

	// As Infants do not require seat, they will be sitting on an Adult's lap. So no action is needed for INFANT.
	return Arrays.stream(ticketTypeRequests)
		.filter(request -> request.getTicketType() != TicketTypeRequest.Type.INFANT)
		.mapToInt(TicketTypeRequest::getNoOfTickets)
		.sum();
    }

    private int computeTotalAmount(TicketTypeRequest... ticketTypeRequests) {
    	return Arrays.stream(ticketTypeRequests)
                .mapToInt(request -> getTicketPrice(request) * request.getNoOfTickets())
                .sum();
    }
    
    private int getTicketPrice(TicketTypeRequest ticketRequest) {
        switch (ticketRequest.getTicketType()) {
            case ADULT:
                return ADULT_TICKET_PRICE;
            case CHILD:
                return CHILD_TICKET_PRICE;
            case INFANT:
                return INFANT_TICKET_PRICE;
            default:
                throw new IllegalArgumentException("Unknown ticket type: " + ticketRequest.getTicketType());
        }
    }
}
