package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.EnumMap;
import java.util.Map;

public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS_PER_PURCHASE = 25;
    private static final int ADULT_TICKET_PRICE = 25;
    private static final int CHILD_TICKET_PRICE = 15;
    /**
     * Should only have private methods other than the one below.
     */

    private final SeatReservationService seatReservationService;
    private final TicketPaymentService ticketPaymentService;

    public TicketServiceImpl(SeatReservationService seatReservationService, TicketPaymentService ticketPaymentService){
        this.seatReservationService = seatReservationService;
        this.ticketPaymentService = ticketPaymentService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);
        Map<Type, Integer> ticketsTotals = summariseTickets(ticketTypeRequests);

        validatebusinessNewRules(ticketsTotals);

        int totalSeats = calculateNumberofSeats(ticketsTotals);
        int totalPrice = calculateTotalAmount(ticketsTotals);

        ticketPaymentService.makePayment(accountId, totalPrice);
        seatReservationService.reserveSeat(accountId, totalSeats);

    }
    private void validateAccountId(Long accountId){
        if(accountId <=0){
            throw new InvalidPurchaseException();
        }
    }

    /**
     * Calculates the total number of ticket requests across ADULT, CHILD and INFANT
     * @return Map<Type, Integer>
     */
    private Map<Type, Integer> summariseTickets(TicketTypeRequest... ticketTypeRequests){
        if(ticketTypeRequests == null || ticketTypeRequests.length == 0){
            throw new InvalidPurchaseException();
        }

        Map<Type, Integer> ticketTotals = new EnumMap<>(Type.class);
        for (Type type: Type.values()){
            ticketTotals.put(type, 0);
        }

        for(TicketTypeRequest ticketTypeRequest: ticketTypeRequests){
            if(ticketTypeRequest == null || ticketTypeRequest.getTicketType() == null){
                throw new InvalidPurchaseException();
            }
            int reqTickets = ticketTypeRequest.getNoOfTickets();
            if (reqTickets<=0){
                throw new InvalidPurchaseException();
            }
            Type ticketType = ticketTypeRequest.getTicketType();
            ticketTotals.put(ticketType, ticketTotals.get(ticketType)+reqTickets);
        }

        return ticketTotals;
    }


    /**
     * Calculates Number of seats to Reserve = Adults + Children
     * Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap
     */
    private int calculateNumberofSeats(Map<Type, Integer> ticketTotals){
        return ticketTotals.get(Type.ADULT)+ticketTotals.get(Type.CHILD);
    }

    /**
     * Calculates the total amount to make payment
     */
    private int calculateTotalAmount(Map<Type, Integer> ticketTotals){
        return (ticketTotals.get(Type.ADULT)* ADULT_TICKET_PRICE)+(ticketTotals.get(Type.CHILD)*CHILD_TICKET_PRICE);
    }

    /**
     * Validate the Business Rules
     * 1. Max Tickets per Purchase is 25
     * 2. Child and Infant tickets cannot be purchased without purchasing an Adult ticket.
     * 3. One Infant ticket per Adult
     */
    private void validatebusinessNewRules(Map<Type, Integer> ticketTotals){

        int adultTickets = ticketTotals.get(Type.ADULT);
        int childTickets = ticketTotals.get(Type.CHILD);
        int infantTickets = ticketTotals.get(Type.INFANT);
        int totalTickets = adultTickets + childTickets + infantTickets;

        if(totalTickets > MAX_TICKETS_PER_PURCHASE){
            throw new InvalidPurchaseException();
        }
        if(adultTickets == 0 && (childTickets > 0 || infantTickets > 0)){
            throw new InvalidPurchaseException();
        }
        if(infantTickets > adultTickets){
            throw new InvalidPurchaseException();
        }
    }

}
