package uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.AccountNumberException;
import uk.gov.dwp.uc.pairtest.exception.InfantAdultTicketsException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.NoAdultTicketException;
import uk.gov.dwp.uc.pairtest.exception.TooManyTicketsException;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

  // Mocked implementations of the third party services
  @Mock
  private TicketPaymentService ticketPaymentService;

  @Mock
  private SeatReservationService seatReservationService;

  // create instance of TicketService with mocked services injected
  @InjectMocks
  private TicketServiceImpl ticketService;

  // tests below are for payment

  @Test
  public void testOneAdultRequestsPaymentOf20() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 1);
    ticketService.purchaseTickets(1L, request);
    verify(ticketPaymentService, times(1)).makePayment(1L, 20);
  }

  @Test
  public void testOneChildWithoutAdultCannotMakeARequest() {
    TicketTypeRequest request = new TicketTypeRequest(Type.CHILD, 1);

    assertThrows(NoAdultTicketException.class, () ->
        ticketService.purchaseTickets(1L, request));
  }

  @Test
  public void testOneInfantWithoutAdultCannotMakeARequest() {
    TicketTypeRequest request = new TicketTypeRequest(Type.INFANT, 1);

    assertThrows(NoAdultTicketException.class, () ->
        ticketService.purchaseTickets(1L, request));
  }

  @Test
  public void testOneAdultOneChildRequestsPaymentOf30() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
    TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 1);

    ticketService.purchaseTickets(1L, adultRequest, childRequest);
    verify(ticketPaymentService, times(1)).makePayment(1L, 30);
  }

  @Test
  public void testOneAdultOneInfantRequestsPaymentOf20() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
    TicketTypeRequest infantRequest = new TicketTypeRequest(Type.INFANT, 1);

    ticketService.purchaseTickets(1L, adultRequest, infantRequest);
    verify(ticketPaymentService, times(1)).makePayment(1L, 20);
  }


  @Test
  public void testMoreInfantsThanAdultsThrowsException() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
    TicketTypeRequest infantRequest = new TicketTypeRequest(Type.INFANT, 2);

    assertThrows(InfantAdultTicketsException.class, () ->
        ticketService.purchaseTickets(1L, adultRequest, infantRequest));
  }

  @Test
  public void testAccountNumberOf0ThrowsException() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 1);

    assertThrows(AccountNumberException.class, () ->
        ticketService.purchaseTickets(0L, request));
  }

  // Tests below are for seat reservation

  @Test
  public void testOneAdultReservesOneSeat() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 1);
    ticketService.purchaseTickets(1L, request);
    verify(seatReservationService, times(1)).reserveSeat(1L, 1);
  }

  @Test
  public void testTwoAdultsReservesTwoSeats() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 2);
    ticketService.purchaseTickets(1L, request);
    verify(seatReservationService, times(1)).reserveSeat(1L, 2);
  }

  @Test
  public void testOneAdultOneChildReservesTwoSeats() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
    TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 1);
    ticketService.purchaseTickets(1L, adultRequest, childRequest);

    verify(seatReservationService, times(1)).reserveSeat(1L, 2);
  }

  @Test
  public void testOneAdultOneInfantReservesOneSeat() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
    TicketTypeRequest infantRequest = new TicketTypeRequest(Type.INFANT, 1);
    ticketService.purchaseTickets(1L, adultRequest, infantRequest);

    verify(seatReservationService, times(1)).reserveSeat(1L, 1);
  }

  @Test
  public void testMoreThan20TicketsCannotBeRequestedAtOnce() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 21);

    assertThrows(TooManyTicketsException.class, () ->
        ticketService.purchaseTickets(1L, request));
  }

  @Test
  public void testMoreThan20TicketsCannotBeRequestedAtOnceDifferentTicketTypes() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 18);
    TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 3);

    assertThrows(TooManyTicketsException.class, () ->
        ticketService.purchaseTickets(1L, adultRequest, childRequest));
  }
}
