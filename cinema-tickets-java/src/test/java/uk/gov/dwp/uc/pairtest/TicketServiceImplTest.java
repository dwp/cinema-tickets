package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;

    @BeforeEach
    public void setUp() {
        paymentService = Mockito.mock(TicketPaymentService.class);
        seatReservationService = Mockito.mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(paymentService, seatReservationService);
    }

    @Test
    public void shouldReserveSeatsCorrectly() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        ticketService.purchaseTickets(1L, adultTicket, childTicket);

        // Verify that 3 seats (2 Adults + 1 Child) are reserved
        verify(seatReservationService, times(1)).reserveSeat(1L, 3);
    }

    @Test
    public void shouldThrowExceptionWhenNoAdultsForChildrenOrInfants() {
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        // Should throw an exception as there are no adults
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, childTicket);
        });
    }

    @Test
    public void shouldNotReserveSeatsForInfants() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(1L, adultTicket, infantTicket);

        // Verify that 2 seats (for Adults) are reserved and infants are not counted
        verify(seatReservationService, times(1)).reserveSeat(1L, 2);
    }

    @Test
    public void shouldThrowExceptionWhenMoreThan25TicketsArePurchased() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26);

        // Should throw an exception as total tickets exceed the maximum limit of 25
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adultTicket);
        });
    }

    @Test
    public void shouldThrowExceptionWhenMoreInfantsThanAdults() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);

        // Should throw an exception as there are more infants than adults
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adultTicket, infantTicket);
        });
    }

    @Test
    public void shouldThrowExceptionWhenAccountIdIsInvalid() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        // Should throw an exception as account ID is invalid
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(0L, adultTicket);
        });

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(null, adultTicket);
        });
    }

    @Test
    public void shouldThrowExceptionWhenOnlyInfantsWithoutAdults() {
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        // Should throw an exception as there are no adults for the infants
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, infantTicket);
        });
    }

    @Test
    public void shouldMakePaymentForCorrectAmount() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);

        ticketService.purchaseTickets(1L, adultTicket, childTicket);

        // Verify that the correct amount (£25 * 2 + £15 * 2 = £80) is paid
        verify(paymentService, times(1)).makePayment(1L, 80);
    }

    @Test
    public void shouldMakeNoPaymentForOnlyInfants() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(1L, adultTicket, infantTicket);

        // Verify that the payment made is only for the adult (£25)
        verify(paymentService, times(1)).makePayment(1L, 25);
    }

    @Test
    public void shouldReserveCorrectSeatsWhenMixOfAdultChildAndInfant() {
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(1L, adultTicket, childTicket, infantTicket);

        // Verify that 3 seats (2 Adults + 1 Child) are reserved
        verify(seatReservationService, times(1)).reserveSeat(1L, 3);
    }

    @Test
    public void shouldThrowExceptionWhenOnlyChildrenWithoutAdults() {
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        // Should throw an exception as there are no adults
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, childTicket);
        });
    }
}
