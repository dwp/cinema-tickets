package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private final SeatReservationService seatReservationService = mock(SeatReservationService.class);
    private final TicketPaymentService paymentService = mock(TicketPaymentService.class);
    private final TicketService service = new TicketServiceImpl(paymentService,seatReservationService);

    @ParameterizedTest(name = "Rejects a purchase when the account ID {0}")
    @NullSource
    @ValueSource(longs = {0L, -1L, -5L})
    @DisplayName("Rejects invalid account ids")
    void shouldRejectNullAccountID(Long accountId){
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(accountId, adultTicket)
        );

        assertEquals(
                "Account ID must be greater than zero",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject null ticket requests")
    void shouldRejectNullTicketTypeRequest(){
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, (TicketTypeRequest[]) null)
        );

        assertEquals(
                "TicketTypeRequests can not be null or empty",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject without ticket requests")
    void shouldRejectWithoutTicketTypeRequest(){
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L)
        );
        assertEquals(
                "TicketTypeRequests can not be null or empty",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject when one of the request is null")
    void shouldRejectWithOneNullTicketTypeRequest(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),  null};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "TicketTypeRequest cannot be null",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);

    }

    @Test
    @DisplayName("Reject when one of the ticket type is null")
    void shouldRejectWithOneNullTicketType(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                new TicketTypeRequest(null, 1)};
        InvalidPurchaseException exception =assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "TicketType cannot be null",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject when number of tickets 0")
    void shouldRejectWhenNumberOfTickets0(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 16)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "number of tickets must be greater than 0",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject when number of tickets Negative")
    void shouldRejectWhenNumberOfTicketsNegative(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -5),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 16)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "number of tickets must be greater than 0",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject when number of tickets more than 25")
    void shouldRejectWhenMoreThan25Tickets(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 15),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "Only a maximum of 25 tickets that can be purchased at a time",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject when Child ticket without Adult")
    void shouldRejectWhenChildTicketWithoutAdult(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "Child and Infant tickets cannot be purchased without purchasing an Adult ticket",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }
    @Test
    @DisplayName("Reject when Infant ticket without Adult")
    void shouldRejectWhenInfantTicketWithoutAdult(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "Child and Infant tickets cannot be purchased without purchasing an Adult ticket",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject when adult ticket(s) not gt or eq to Infant(s)")
    void shouldRejectWhenInfantTicketsMoreThanAdults(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 9)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "Number of Infant tickets cannot be more than number of Adult tickets",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Reject large ticket quantities without overflow")
    void shouldRejectWhenLargeNumberOfTickets(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, Integer.MAX_VALUE),
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, Integer.MAX_VALUE)};
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        assertEquals(
                "Only a maximum of 25 tickets that can be purchased at a time",
                exception.getMessage()
        );
        verifyNoInteractions(seatReservationService);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("Calculate valid requests")
    void shouldRequestValidSeatsAndPayment(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10)};
        assertDoesNotThrow(
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );

        verify(seatReservationService).reserveSeat(1L, 20);
        verify(paymentService).makePayment(1L, 400);
    }

    @Test
    @DisplayName("Calculate valid requests when more TicketTypeRequests are passed ")
    void shouldRequestMultipleTicketTypeRequests(){
        TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        assertDoesNotThrow(
                () -> service.purchaseTickets(1L, ticketTypeRequest1, ticketTypeRequest2, ticketTypeRequest3)
        );
        verify(seatReservationService).reserveSeat(1L, 5);
        verify(paymentService).makePayment(1L, 95);
    }

    @Test
    @DisplayName("Calculate Requests with exact 25 Adult tickets")
    void shouldCalculate25AdultTickets(){
        TicketTypeRequest[] ticketTypeRequests = {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15)};
        assertDoesNotThrow(
                () -> service.purchaseTickets(1L, ticketTypeRequests)
        );
        verify(seatReservationService).reserveSeat(1L, 25);
        verify(paymentService).makePayment(1L, 625);
    }
}