package uk.gov.dwp.uc.pairtest;

import java.util.HashMap;
import java.util.Map;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationService;

    private final int ADULT_TICKET_PRICE = 20;
    private final int CHILD_TICKET_PRICE = 10;
    private final int INFANT_TICKET_PRICE = 0;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        // generate Map of ticket type and number of tickets for the TicketTypeRequest vararg
        Map<Type, Integer> mapOfTicketsPerType = getMapOfTicketsPerType(ticketTypeRequests);

        // check whether the requests are valid and throw appropriate exceptions if not
        validatePurchaseRequest(mapOfTicketsPerType, accountId);

        // make request to payment service
        int totalPaymentAmount = calculateTotalPaymentAmount(mapOfTicketsPerType);
        ticketPaymentService.makePayment(accountId, totalPaymentAmount);

        //make request to seat reservation service
        int numberOfAdultTickets = mapOfTicketsPerType.get(Type.ADULT);
        seatReservationService.reserveSeat(accountId, numberOfAdultTickets);
    }

    private int calculateTotalPaymentAmount(Map<Type, Integer> mapOfTicketsPerType) {
        int adultTicketAmount = mapOfTicketsPerType.get(Type.ADULT) * ADULT_TICKET_PRICE;
        int childTicketAmount = mapOfTicketsPerType.get(Type.CHILD) * CHILD_TICKET_PRICE;
        int infantTicketAmount = mapOfTicketsPerType.get(Type.INFANT) * INFANT_TICKET_PRICE;

        return adultTicketAmount + childTicketAmount + infantTicketAmount;
    }

    private void validatePurchaseRequest(Map<Type, Integer> mapOfTicketsPerType, Long accountId) {
        int numberOfAdultTickets = mapOfTicketsPerType.get(Type.ADULT);
        int numberOfChildTickets = mapOfTicketsPerType.get(Type.CHILD);
        int numberOfInfantTickets = mapOfTicketsPerType.get(Type.INFANT);

        if (accountId <= 0) {
            throw new InvalidPurchaseException("ERROR: Account number must be greater than 0");
        } else if ((numberOfChildTickets > 0 || numberOfInfantTickets > 0) && numberOfAdultTickets == 0) {
            throw new InvalidPurchaseException("ERROR: At least one adult ticket is required when purchasing a child/infant ticket");
        } else if (numberOfInfantTickets > numberOfAdultTickets) {
            throw new InvalidPurchaseException("ERROR: Each infant ticket must be accompanied by an adult ticket");
        }
    }

    private Map<Type, Integer> getMapOfTicketsPerType(TicketTypeRequest... ticketTypeRequests) {
        Map<Type, Integer> mapOfTicketsPerType = new HashMap<>(Map.of(Type.ADULT, 0, Type.CHILD, 0, Type.INFANT, 0));

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            int numberOfSeats = mapOfTicketsPerType.get(ticketTypeRequest.getTicketType());
            Type ticketType = ticketTypeRequest.getTicketType();
            mapOfTicketsPerType.put(ticketType, numberOfSeats + ticketTypeRequest.getNoOfTickets());
        }

        return mapOfTicketsPerType;
    }
}
