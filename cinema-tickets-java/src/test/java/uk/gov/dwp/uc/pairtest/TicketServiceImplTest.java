package uk.gov.dwp.uc.pairtest;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

  private TicketTypeRequest[] ticketRequests;

  @Mock
  TicketPaymentService ticketPaymentService;

  @Mock
  SeatReservationService seatReservationService;

  @InjectMocks
  TicketServiceImpl ticketService;

  @Test
  public void testInvalidAccountId() {
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3);

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(0L, newRequest));

    Assertions.assertEquals(403, exception.getStatusCode());
    Assertions.assertEquals("Invalid account ID supplied. Unauthorised to access this service.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testInvalidNoAdultTicketType() {
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, newRequest));

    Assertions.assertEquals(400, exception.getStatusCode());
    Assertions.assertEquals("Your ticket purchase must contain at least one adult ticket.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testInvalidMultipleNoAdultTicketTypes() {
    ticketRequests = new TicketTypeRequest[2];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3);
    ticketRequests[1] = newRequest;

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, ticketRequests));

    Assertions.assertEquals(400, exception.getStatusCode());
    Assertions.assertEquals("Your ticket purchase must contain at least one adult ticket.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testInvalidMoreInfantsThanAdults() {
    ticketRequests = new TicketTypeRequest[2];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3);
    ticketRequests[1] = newRequest;

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, ticketRequests));

    Assertions.assertEquals(400, exception.getStatusCode());
    Assertions.assertEquals("There must be at least one adult ticket purchased for every infant ticket.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testInvalidMoreThan20TicketsPurchased() {
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, newRequest));

    Assertions.assertEquals(400, exception.getStatusCode());
    Assertions.assertEquals("A maximum of 20 tickets are allowed to be purchased at a time.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testInvalidMoreThan20TicketsPurchasedFromDifferentTypes() {
    ticketRequests = new TicketTypeRequest[2];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 6);
    ticketRequests[1] = newRequest;

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, ticketRequests));

    Assertions.assertEquals(400, exception.getStatusCode());
    Assertions.assertEquals("A maximum of 20 tickets are allowed to be purchased at a time.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testInvalidNoTicketsBought() {
    ticketRequests = new TicketTypeRequest[2];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
    ticketRequests[1] = newRequest;

    InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, ticketRequests));

    Assertions.assertEquals(400, exception.getStatusCode());
    Assertions.assertEquals("Your ticket purchase must contain at least one adult ticket.", exception.getExceptionMessage());

    verifyNoInteractions(ticketPaymentService);
    verifyNoInteractions(seatReservationService);
  }

  @Test
  public void testValidAdultSingleTicketBooking() {

    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

    ticketService.purchaseTickets(1L, newRequest);

    verify(ticketPaymentService, times(1)).makePayment(eq(1L), eq(20));
    verify(seatReservationService, times(1)).reserveSeat(eq(1L), eq(1));
  }

  @Test
  public void testValidAdultAndChildTicketBooking() {
    ticketRequests = new TicketTypeRequest[2];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
    ticketRequests[1] = newRequest;

    ticketService.purchaseTickets(1L, ticketRequests);

    verify(ticketPaymentService, times(1)).makePayment(eq(1L), eq(30));
    verify(seatReservationService, times(1)).reserveSeat(eq(1L), eq(2));
  }

  @Test
  public void testValidAdultInfantAndChildTicketBooking() {
    ticketRequests = new TicketTypeRequest[3];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
    ticketRequests[1] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
    ticketRequests[2] = newRequest;

    ticketService.purchaseTickets(1L, ticketRequests);

    verify(ticketPaymentService, times(1)).makePayment(eq(1L), eq(30));
    verify(seatReservationService, times(1)).reserveSeat(eq(1L), eq(2));
  }

  @Test
  public void testValid20TicketBooking() {
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20);

    ticketService.purchaseTickets(1L, newRequest);

    verify(ticketPaymentService, times(1)).makePayment(eq(1L), eq(400));
    verify(seatReservationService, times(1)).reserveSeat(eq(1L), eq(20));
  }

  @Test
  public void testValid20TicketFromMultipleTypesBooking() {
    ticketRequests = new TicketTypeRequest[3];
    TicketTypeRequest newRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
    ticketRequests[0] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5);
    ticketRequests[1] = newRequest;
    newRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
    ticketRequests[2] = newRequest;

    ticketService.purchaseTickets(1L, ticketRequests);

    verify(ticketPaymentService, times(1)).makePayment(eq(1L), eq(250));
    verify(seatReservationService, times(1)).reserveSeat(eq(1L), eq(15));
  }
}