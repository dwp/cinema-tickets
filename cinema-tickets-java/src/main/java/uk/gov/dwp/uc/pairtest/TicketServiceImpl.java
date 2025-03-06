// Source code is decompiled from a .class file using FernFlower decompiler.
package uk.gov.dwp.uc.pairtest;

import java.util.Arrays;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
   private static final int MAX_TICKETS = 25;
   private static final int ADULT_TICKET_PRICE = 25;
   private static final int CHILD_TICKET_PRICE = 15;
   private static final int INFANT_TICKET_PRICE = 0;
   private final TicketPaymentService ticketPaymentService;
   private final SeatReservationService seatReservationService;

   public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
      this.ticketPaymentService = ticketPaymentService;
      this.seatReservationService = seatReservationService;
   }

   public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
      this.validateAccount(accountId);
      this.validateTicketRequest(ticketTypeRequests);
      int totalSeats = this.computeTotalSeats(ticketTypeRequests);
      int totalAmount = this.computeTotalAmount(ticketTypeRequests);
      this.ticketPaymentService.makePayment(accountId, totalAmount);
      this.seatReservationService.reserveSeat(accountId, totalSeats);
      System.out.println("Tickets successfully purchased for Account ID: " + String.valueOf(accountId));
   }

   private void validateAccount(Long accountId) {
      if (accountId == null || accountId <= 0L) {
         throw new InvalidPurchaseException(" Invalid account ID: " + String.valueOf(accountId));
      }
   }

   private void validateTicketRequest(TicketTypeRequest... ticketRequests) {
      int totalTickets = Arrays.stream(ticketRequests).mapToInt(TicketTypeRequest::getNoOfTickets).sum();
      if (totalTickets > 25) {
         throw new InvalidPurchaseException(" Cannot purchase more than 25 tickets.");
      } else {
         int adultCount = this.getTicketCount(ticketRequests, Type.ADULT);
         int childCount = this.getTicketCount(ticketRequests, Type.CHILD);
         int infantCount = this.getTicketCount(ticketRequests, Type.INFANT);
         if ((childCount > 0 || infantCount > 0) && adultCount == 0) {
            throw new InvalidPurchaseException(" Child or Infant tickets require at least one Adult ticket.");
         } else if (Arrays.stream(ticketRequests).anyMatch((request) -> {
            return request.getNoOfTickets() < 0;
         })) {
            throw new InvalidPurchaseException(" Ticket quantity cannot be negative.");
         }
      }
   }

   private int computeTotalSeats(TicketTypeRequest... ticketRequests) {
      return Arrays.stream(ticketRequests).filter((request) -> {
         return request.getTicketType() != Type.INFANT;
      }).mapToInt(TicketTypeRequest::getNoOfTickets).sum();
   }

   private int computeTotalAmount(TicketTypeRequest... ticketRequests) {
      return Arrays.stream(ticketRequests).mapToInt((request) -> {
         return this.getTicketPrice(request.getTicketType()) * request.getNoOfTickets();
      }).sum();
   }

   private int getTicketPrice(TicketTypeRequest.Type type) {
      switch (type) {
         case ADULT:
            return 25;
         case CHILD:
            return 15;
         case INFANT:
            return 0;
         default:
            throw new IllegalArgumentException(" Unknown ticket type: " + String.valueOf(type));
      }
   }

   private int getTicketCount(TicketTypeRequest[] ticketRequests, TicketTypeRequest.Type type) {
      return Arrays.stream(ticketRequests).filter((request) -> {
         return request.getTicketType() == type;
      }).mapToInt(TicketTypeRequest::getNoOfTickets).sum();
   }
}
