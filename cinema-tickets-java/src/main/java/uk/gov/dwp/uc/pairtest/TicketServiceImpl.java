package uk.gov.dwp.uc.pairtest;

import java.util.HashMap;
import java.util.Map;
import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private TicketPaymentService ticketPaymentService;

    private final int ADULT_TICKET_PRICE = 20;
    private final int CHILD_TICKET_PRICE = 10;
    private final int INFANT_TICKET_PRICE = 0;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService) {
        this.ticketPaymentService = ticketPaymentService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        Map<Type, Integer> mapOfTicketsPerType = getMapOfTicketsPerType(ticketTypeRequests);

        int numberOfAdultTickets = mapOfTicketsPerType.get(Type.ADULT);
        int numberOfChildTickets = mapOfTicketsPerType.get(Type.CHILD);
        int numberOfInfantTickets = mapOfTicketsPerType.get(Type.INFANT);

        if ((numberOfChildTickets > 0 || numberOfInfantTickets > 0) && numberOfAdultTickets == 0) {
            throw new InvalidPurchaseException("ERROR: At least one adult ticket is required when purchasing a child/infant ticket");
        } else if (numberOfInfantTickets > numberOfAdultTickets) {
            throw new InvalidPurchaseException("ERROR: Each infant ticket must be accompanied by an adult ticket");
        }

        int totalPaymentAmount =
            (numberOfAdultTickets * ADULT_TICKET_PRICE) +
                (numberOfChildTickets * CHILD_TICKET_PRICE) +
                (numberOfInfantTickets * INFANT_TICKET_PRICE);

        ticketPaymentService.makePayment(accountId, totalPaymentAmount);
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
