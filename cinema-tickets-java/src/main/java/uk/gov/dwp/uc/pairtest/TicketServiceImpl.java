package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

public class TicketServiceImpl implements TicketService {

    /**
     * Should only have private methods other than the one below.
     */

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private static final int ADULT_TICKET_PRICE = 25;
    private static final int CHILD_TICKET_PRICE = 15;
    private static final int MAX_TICKETS_PER_PURCHASE = 25;



    public TicketServiceImpl(TicketPaymentService paymentService,
                             SeatReservationService seatReservationService ) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);
        validateTicketTypeRequest(ticketTypeRequests);
        PurchaseSummary summary = getPurchaseSummary(ticketTypeRequests);

        seatReservationService.reserveSeat(accountId, summary.seats);

        paymentService.makePayment(accountId, summary.amount);

    }

    private void validateAccountId(Long accountId){
        if(accountId == null || accountId <= 0)
            throw new InvalidPurchaseException("Account ID must be greater than zero");
    }

    private void validateTicketTypeRequest(TicketTypeRequest... ticketTypeRequests){
        if(ticketTypeRequests == null || ticketTypeRequests.length == 0)
            throw new InvalidPurchaseException("TicketTypeRequests can not be null or empty");

        long adultTicketCounts = 0;
        long childTicketCounts = 0;
        long infantTicketCounts = 0;

        for(TicketTypeRequest ticketTypeRequest : ticketTypeRequests){
            if(ticketTypeRequest == null )
                throw new InvalidPurchaseException("TicketTypeRequest cannot be null");

            if(ticketTypeRequest.getTicketType() == null )
                throw new InvalidPurchaseException("TicketType cannot be null");

            int numberOfTickets = ticketTypeRequest.getNoOfTickets();
            if(numberOfTickets  <= 0 )
                throw new InvalidPurchaseException("number of tickets must be greater than 0");

            switch (ticketTypeRequest.getTicketType()){
                case ADULT -> adultTicketCounts +=  numberOfTickets;
                case CHILD -> childTicketCounts += numberOfTickets;
                case INFANT -> infantTicketCounts += numberOfTickets;
            }
        }
        if((adultTicketCounts + childTicketCounts + infantTicketCounts ) > MAX_TICKETS_PER_PURCHASE){
            throw new InvalidPurchaseException("Only a maximum of 25 tickets that can be purchased at a time");
        }

        if((childTicketCounts > 0 || infantTicketCounts > 0) && adultTicketCounts ==0){
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without purchasing an Adult ticket");
        }
    }

    private record PurchaseSummary(int amount, int seats){}

    private PurchaseSummary getPurchaseSummary(TicketTypeRequest... ticketTypeRequests){
        int adultCount = Arrays.stream(ticketTypeRequests)
                .filter(req -> (req.getTicketType() == TicketTypeRequest.Type.ADULT))
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
        int childCount = Arrays.stream(ticketTypeRequests)
                .filter(req -> (req.getTicketType() == TicketTypeRequest.Type.CHILD))
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
        int totalAmount = (adultCount * ADULT_TICKET_PRICE) +
                (childCount * CHILD_TICKET_PRICE);
        int totalSeats = adultCount + childCount;
        return new PurchaseSummary(totalAmount, totalSeats);
    }


}
