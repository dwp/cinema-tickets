package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class TicketServiceImplTest {

    private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp(){
        ticketPaymentService = mock(TicketPaymentService.class);
        seatReservationService = mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(seatReservationService, ticketPaymentService);
    }

    /**
     * Valid Request test
     */
    @Test
    void validTicketRequestTest(){
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(Type.ADULT, 2),
                new TicketTypeRequest(Type.CHILD,3 ),
                new TicketTypeRequest(Type.INFANT, 1));

        verify(ticketPaymentService).makePayment(1L, 95);
        verify(seatReservationService).reserveSeat(1L, 5);
    }

    /**
     * Invalid Request test
     * Infant tickets exceed more than adult tickets
     */
    @Test
    void inValidInfantTicketRequestTest(){
        assertInvalidPurchase(1L,
                new TicketTypeRequest(Type.ADULT, 2),
                new TicketTypeRequest(Type.CHILD,3),
                new TicketTypeRequest(Type.INFANT, 4));
    }

    /**
     * Invalid Child only ticket Request Test
     */
    @Test
    void inValidChildOnlyRequestTest(){
        assertInvalidPurchase(8L,
                new TicketTypeRequest(Type.CHILD,3 ));
    }

    /**
     * Invalid account id Request Test
     */
    @Test
    void inValidAccountIdRequestTest(){
        assertInvalidPurchase(0L,
                new TicketTypeRequest(Type.ADULT,3 ));
    }

    /**
     * invalid zero seat reserve ticket request Test
     */
    @Test
    void inValidNonPositiveTicketRequest() {
        assertInvalidPurchase(
                1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0)
        );
    }

    /**
     * invalid request to reserve more seats than the set limit Test
     */
    @Test
    void inValidMoreThanTwentyFiveTicketsRequest() {
        assertInvalidPurchase(
                1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26)
        );
    }

    /**
     * asserts Invalid Purchases
     */
    private void assertInvalidPurchase(Long accountId, TicketTypeRequest... requests) {
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountId, requests));
        verify(ticketPaymentService, never()).makePayment(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyInt());
        verify(seatReservationService, never()).reserveSeat(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyInt());
    }

}
