package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        paymentService = mock(TicketPaymentService.class);
        seatReservationService = mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(paymentService, seatReservationService);
    }

    @Test
    void testPurchaseTickets_ValidAdultAndChild() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        ticketService.purchaseTickets(1L, adultRequest, childRequest);

        verify(paymentService).makePayment(1L, 40);
        verify(seatReservationService).reserveSeat(1L, 2);
    }

    @Test
    void testPurchaseTickets_ValidAdultChildAndInfant() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(1L, adultRequest, childRequest, infantRequest);

        verify(paymentService).makePayment(1L, 40);
        verify(seatReservationService).reserveSeat(1L, 2);
    }

    @Test
    void testPurchaseTickets_ValidMultipleAdultsAndChildren() {
        TicketTypeRequest adultRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);

        ticketService.purchaseTickets(1L, adultRequest1, childRequest1);

        verify(paymentService).makePayment(1L, 95);
        verify(seatReservationService).reserveSeat(1L, 5);
    }

    @Test
    void testPurchaseTickets_TooManyTickets() {
        TicketTypeRequest[] requests = new TicketTypeRequest[26];
        for (int i = 0; i < 26; i++) {
            requests[i] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        }

        InvalidPurchaseException exception = org.junit.jupiter.api.Assertions.assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L, requests)
        );

        assertEquals("Cannot purchase more than 25 tickets.", exception.getMessage());
    }

    @Test
    void testPurchaseTickets_NoAdultTickets() {
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        InvalidPurchaseException exception = org.junit.jupiter.api.Assertions.assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L, childRequest)
        );

        assertEquals("Child or infant tickets cannot be purchased without an adult ticket.", exception.getMessage());
    }

    @Test
    void testPurchaseTickets_ChildWithoutAdult() {
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        InvalidPurchaseException exception = org.junit.jupiter.api.Assertions.assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L, childRequest, infantRequest)
        );

        assertEquals("Child or infant tickets cannot be purchased without an adult ticket.", exception.getMessage());
    }

}
