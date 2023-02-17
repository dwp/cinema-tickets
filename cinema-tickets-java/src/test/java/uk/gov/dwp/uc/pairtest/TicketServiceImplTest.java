package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.CHILD;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.INFANT;

class TicketServiceImplTest {

  private static final Long ACCOUNT_ID = 1L;
  private final TicketPaymentService ticketPaymentService = mock(TicketPaymentService.class);
  private final SeatReservationService seatReservationService = mock(SeatReservationService.class);
  private final TicketServiceImpl underTest =
      new TicketServiceImpl(ticketPaymentService, seatReservationService);

  @ParameterizedTest
  @MethodSource("makePayment_totalAmountToPay")
  void makePayment_CallsTicketPaymentServiceWithAccountIdAndTotalAmountToPay(
      TicketTypeRequest[] ticketTypeRequests, int expectedTotalAmountToPay) {
    underTest.purchaseTickets(ACCOUNT_ID, ticketTypeRequests);

    verify(ticketPaymentService, times(1)).makePayment(ACCOUNT_ID, expectedTotalAmountToPay);
  }

  @ParameterizedTest
  @MethodSource("makePayment_totalSeatsToAllocate")
  void makePayment_CallsSeatReservationServiceWithAccountIdAndTotalSeatsToAllocate(
      TicketTypeRequest[] ticketTypeRequests, int expectedTotalSeatsToAllocate) {
    underTest.purchaseTickets(ACCOUNT_ID, ticketTypeRequests);

    verify(seatReservationService, times(1)).reserveSeat(ACCOUNT_ID, expectedTotalSeatsToAllocate);
  }

  @Test
  void makePayment_ThrowsExceptionWhenIdIsLessThanOne() {
    TicketTypeRequest[] ticketTypeRequests = {
      new TicketTypeRequest(INFANT, 5),
      new TicketTypeRequest(CHILD, 5),
      new TicketTypeRequest(ADULT, 10)
    };

    InvalidPurchaseException exception =
        assertThrows(
            InvalidPurchaseException.class,
            () -> underTest.purchaseTickets(0L, ticketTypeRequests));

    assertEquals("Invalid accountId. accountId should be greater than 0.", exception.getMessage());
  }

  @Test
  void makePayment_ThrowsExceptionWhenNoOfTicketsIsGreaterThanTwenty() {
    TicketTypeRequest[] ticketTypeRequests = {
      new TicketTypeRequest(INFANT, 7),
      new TicketTypeRequest(CHILD, 8),
      new TicketTypeRequest(ADULT, 9)
    };

    InvalidPurchaseException exception =
        assertThrows(
            InvalidPurchaseException.class,
            () -> underTest.purchaseTickets(ACCOUNT_ID, ticketTypeRequests));

    assertEquals(
        "Invalid purchase tickets request. Total number of tickets must not exceed 20.",
        exception.getMessage());
  }

  @Test
  void makePayment_ThrowsExceptionWhenInfantOrChildTicketsArePurchasedWithoutAdultTickets() {
    TicketTypeRequest[] ticketTypeRequests = {
      new TicketTypeRequest(INFANT, 5), new TicketTypeRequest(CHILD, 5)
    };

    InvalidPurchaseException exception =
        assertThrows(
            InvalidPurchaseException.class,
            () -> underTest.purchaseTickets(ACCOUNT_ID, ticketTypeRequests));

    assertEquals(
        "Invalid purchase tickets request. Infant or child tickets must be purchased with at least"
            + " 1 adult ticket.",
        exception.getMessage());
  }

  @Test
  void makePayment_ThrowsExceptionWhenMoreInfantTicketsArePurchasedThanAdultTickets() {
    TicketTypeRequest[] ticketTypeRequests = {
      new TicketTypeRequest(INFANT, 10),
      new TicketTypeRequest(CHILD, 5),
      new TicketTypeRequest(ADULT, 5)
    };

    InvalidPurchaseException exception =
        assertThrows(
            InvalidPurchaseException.class,
            () -> underTest.purchaseTickets(ACCOUNT_ID, ticketTypeRequests));

    assertEquals(
        "Invalid purchase tickets request. Infant tickets should be less than or equal to adult "
            + "tickets.",
        exception.getMessage());
  }

  private static Stream<Arguments> makePayment_totalAmountToPay() {
    return Stream.of(
        arguments(
            new TicketTypeRequest[] {
              new TicketTypeRequest(INFANT, 5),
              new TicketTypeRequest(CHILD, 5),
              new TicketTypeRequest(ADULT, 10)
            },
            250),
        arguments(
            new TicketTypeRequest[] {
              new TicketTypeRequest(ADULT, 1),
              new TicketTypeRequest(ADULT, 5),
              new TicketTypeRequest(ADULT, 2)
            },
            160),
        arguments(
            new TicketTypeRequest[] {
              new TicketTypeRequest(CHILD, 2),
              new TicketTypeRequest(CHILD, 5),
              new TicketTypeRequest(ADULT, 1),
              new TicketTypeRequest(ADULT, 4)
            },
            170));
  }

  private static Stream<Arguments> makePayment_totalSeatsToAllocate() {
    return Stream.of(
        arguments(
            new TicketTypeRequest[] {
              new TicketTypeRequest(INFANT, 5),
              new TicketTypeRequest(CHILD, 5),
              new TicketTypeRequest(ADULT, 10)
            },
            15),
        arguments(
            new TicketTypeRequest[] {
              new TicketTypeRequest(ADULT, 1),
              new TicketTypeRequest(ADULT, 5),
              new TicketTypeRequest(ADULT, 2)
            },
            8),
        arguments(
            new TicketTypeRequest[] {
              new TicketTypeRequest(CHILD, 2),
              new TicketTypeRequest(CHILD, 5),
              new TicketTypeRequest(ADULT, 1),
              new TicketTypeRequest(ADULT, 4)
            },
            12));
  }
}
