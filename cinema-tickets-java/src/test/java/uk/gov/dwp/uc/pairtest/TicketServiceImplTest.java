// Source code is decompiled from a .class file using FernFlower decompiler.
package uk.gov.dwp.uc.pairtest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {
   @Mock
   private TicketPaymentService ticketPaymentService;
   @Mock
   private SeatReservationService seatReservationService;
   @InjectMocks
   private TicketServiceImpl ticketService;

   public TicketServiceImplTest() {
   }

   @Before
   public void setUp() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void shouldPurchaseTicketsSuccessfully() {
      Long accountId = 1L;
      TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 2);
      TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 1);
      this.ticketService.purchaseTickets(accountId, new TicketTypeRequest[]{adultTicket, childTicket});
      ((TicketPaymentService)Mockito.verify(this.ticketPaymentService, Mockito.times(1))).makePayment(accountId, 65);
      ((SeatReservationService)Mockito.verify(this.seatReservationService, Mockito.times(1))).reserveSeat(accountId, 3);
   }

   @Test
   public void shouldThrowExceptionForInvalidAccountId() {
      Long invalidAccountId = -1L;
      TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 1);

      try {
         this.ticketService.purchaseTickets(invalidAccountId, new TicketTypeRequest[]{adultTicket});
      } catch (InvalidPurchaseException var4) {
         Assert.assertEquals(" Invalid account ID: -1", var4.getMessage());
      }

   }

   @Test
   public void shouldThrowExceptionForExceedingMaxTickets() {
      Long accountId = 1L;
      TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 26);

      try {
         this.ticketService.purchaseTickets(accountId, new TicketTypeRequest[]{adultTicket});
      } catch (InvalidPurchaseException var4) {
         Assert.assertEquals(" Cannot purchase more than 25 tickets.", var4.getMessage());
      }

   }

   @Test
   public void shouldThrowExceptionForChildOrInfantWithoutAdult() {
      Long accountId = 1L;
      TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 2);
      TicketTypeRequest infantTicket = new TicketTypeRequest(Type.INFANT, 1);

      try {
         this.ticketService.purchaseTickets(accountId, new TicketTypeRequest[]{childTicket, infantTicket});
      } catch (InvalidPurchaseException var5) {
         Assert.assertEquals(" Child or Infant tickets require at least one Adult ticket.", var5.getMessage());
      }

   }

   @Test
   public void shouldThrowExceptionForNegativeTicketCount() {
      Long accountId = 1L;
      TicketTypeRequest invalidTicket = new TicketTypeRequest(Type.ADULT, -2);

      try {
         this.ticketService.purchaseTickets(accountId, new TicketTypeRequest[]{invalidTicket});
      } catch (InvalidPurchaseException var4) {
         Assert.assertEquals(" Ticket quantity cannot be negative.", var4.getMessage());
      }

   }

   @Test
   public void shouldAllowOnlyAdultTicketPurchase() {
      Long accountId = 1L;
      TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 3);
      this.ticketService.purchaseTickets(accountId, new TicketTypeRequest[]{adultTicket});
      ((TicketPaymentService)Mockito.verify(this.ticketPaymentService, Mockito.times(1))).makePayment(accountId, 75);
      ((SeatReservationService)Mockito.verify(this.seatReservationService, Mockito.times(1))).reserveSeat(accountId, 3);
   }

   @Test
   public void shouldPurchaseInfantWithAdultSuccessfully() {
      Long accountId = 1L;
      TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 2);
      TicketTypeRequest infantTicket = new TicketTypeRequest(Type.INFANT, 2);
      this.ticketService.purchaseTickets(accountId, new TicketTypeRequest[]{adultTicket, infantTicket});
      ((TicketPaymentService)Mockito.verify(this.ticketPaymentService, Mockito.times(1))).makePayment(accountId, 50);
      ((SeatReservationService)Mockito.verify(this.seatReservationService, Mockito.times(1))).reserveSeat(accountId, 2);
   }
}
