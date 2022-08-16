package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private TicketPaymentService ticketPaymentService;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService) {
        this.ticketPaymentService = ticketPaymentService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        int numberOfAdultTickets = 0;
        int numberOfChildTickets = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            if (ticketTypeRequest.getTicketType() == Type.ADULT) {
                numberOfAdultTickets += ticketTypeRequest.getNoOfTickets();
            } else if (ticketTypeRequest.getTicketType() == Type.CHILD) {
                numberOfChildTickets += ticketTypeRequest.getNoOfTickets();
            }
        }

        if (numberOfChildTickets != 0 && numberOfAdultTickets == 0) {
            throw new InvalidPurchaseException("ERROR: At least one adult ticket is required when purchasing a child/infant ticket");
        }
        ticketPaymentService.makePayment(accountId, 20);
    }
}
