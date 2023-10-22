package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketPaymentService ticketPaymentService;
    @Mock
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketServiceImpl;

    @BeforeEach
    public void setUp() {
        ticketServiceImpl = new TicketServiceImpl(ticketPaymentService, seatReservationService);
    }

    @Test
    void shouldMakePaymentAndReserveSeat_whenValidRequest() throws InvalidPurchaseException {
        long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(CHILD, 1);
        TicketTypeRequest infantTicket = new TicketTypeRequest(INFANT, 1);

        ticketServiceImpl.purchaseTickets(accountId, adultTicket, childTicket, infantTicket);

        verify(ticketPaymentService).makePayment(accountId, 50);
        verify(seatReservationService).reserveSeat(accountId, 3);
    }

    @Test
    void shouldMakePaymentAndReserveSeat_whenOnlyAnAdultTicket() {
        long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 1);

        ticketServiceImpl.purchaseTickets(accountId, adultTicket);

        verify(ticketPaymentService).makePayment(accountId, 20);
        verify(seatReservationService).reserveSeat(accountId, 1);
    }

    @Test
    void shouldMakePaymentAndReserveSeat_when10AdultsAnd10InfantTickets() {
        long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 10);
        TicketTypeRequest infantTicket = new TicketTypeRequest(INFANT, 10);

        ticketServiceImpl.purchaseTickets(accountId, adultTicket, infantTicket);

        verify(ticketPaymentService).makePayment(accountId, 200);
        verify(seatReservationService).reserveSeat(accountId, 10);
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenInvalidAccountId() throws InvalidPurchaseException {
        Long accountId = -1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 1);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adultTicket));

        Assertions.assertEquals("Invalid Account ID.", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenMoreThan20TicketsRequested()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 15);
        TicketTypeRequest childTicket = new TicketTypeRequest(CHILD, 4);
        TicketTypeRequest infantTicket = new TicketTypeRequest(INFANT, 2);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adultTicket, childTicket, infantTicket));

        Assertions.assertEquals("Cannot purchase more than 20 tickets at a time.",
                exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenChildOrInfantTicketsWithoutAdultTicket()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest childTicket = new TicketTypeRequest(CHILD, 1);
        TicketTypeRequest infantTicket = new TicketTypeRequest(INFANT, 1);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, childTicket, infantTicket));

        Assertions.assertEquals("Cannot purchase child or infant tickets without purchasing " +
                        "an adult ticket.",
                exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenChildTicketsWithoutAdultTicket()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest childTicket = new TicketTypeRequest(CHILD, 1);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, childTicket));

        Assertions.assertEquals("Cannot purchase child or infant tickets without purchasing " +
                        "an adult ticket.",
                exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenInfantTicketsWithoutAdultTicket()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest infantTicket = new TicketTypeRequest(INFANT, 1);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, infantTicket));

        Assertions.assertEquals("Cannot purchase child or infant tickets without purchasing " +
                        "an adult ticket.",
                exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenMoreInfantTicketsRequestedThanAdult()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 1);
        TicketTypeRequest infantTicket = new TicketTypeRequest(INFANT, 2);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adultTicket, infantTicket));

        Assertions.assertEquals("More infants tickets than adult tickets.",
                exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenNullPassedAsTicketsTypeRequests()
            throws InvalidPurchaseException {
        Long accountId = 1L;

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, null));

        Assertions.assertEquals("No ticket request made.", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenNumberOfTicketsIsZero()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 0);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adultTicket));

        Assertions.assertEquals("Number of tickets to be purchased must be positive.",
                exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenTicketTypeRequestTypeIsNull()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(null, 1);

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adultTicket));

        Assertions.assertEquals("Ticket type is null.", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenOneTicketTypeRequestIsNull()
            throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(ADULT, 1);
        TicketTypeRequest childTicket = null;

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adultTicket, childTicket));

        Assertions.assertEquals("Ticket type request is not valid.", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidPurchaseException_whenTicketTypeRequestIsNotGiven()
            throws InvalidPurchaseException {
        Long accountId = 1L;

        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId));

        Assertions.assertEquals("No ticket request made.", exception.getMessage());
    }
}