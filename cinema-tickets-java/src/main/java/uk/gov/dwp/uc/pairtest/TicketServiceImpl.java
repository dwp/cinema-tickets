package uk.gov.dwp.uc.pairtest;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketServiceValidator;

@Validated
public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        TicketServiceValidator.validateAccountId(accountId);

        Map<TicketTypeRequest.Type, Integer> tickets = getTickets(ticketTypeRequests);

        TicketServiceValidator.validateTicketsRequest(tickets);

        seatReservationService.reserveSeat(accountId, getNumberOfSeats(tickets));

        ticketPaymentService.makePayment(accountId, getTotalCost(tickets));
    }

    private Map<TicketTypeRequest.Type, Integer> getTickets(TicketTypeRequest... ticketTypeRequests) {

        return Arrays.stream(ticketTypeRequests)
            .collect(Collectors
            .toMap(TicketTypeRequest::getTicketType, TicketTypeRequest::getNoOfTickets, Integer::sum));
    }

    private int getNumberOfSeats(Map<TicketTypeRequest.Type, Integer> tickets) {

        return tickets.entrySet().stream()
            .filter((ticket) -> !ticket.getKey().equals(TicketTypeRequest.Type.INFANT))
            .mapToInt(Map.Entry::getValue)
            .sum();
    }

    private int getTotalCost(Map<TicketTypeRequest.Type, Integer> tickets) {

        return tickets.entrySet().stream()
            .mapToInt((ticketTypeRequest) -> ticketTypeRequest.getKey().getCost() * ticketTypeRequest.getValue())
            .sum();
    }

}
