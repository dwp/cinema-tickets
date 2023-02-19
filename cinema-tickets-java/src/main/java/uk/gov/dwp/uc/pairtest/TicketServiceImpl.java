package uk.gov.dwp.uc.pairtest;

import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.INFANT;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {

  private final TicketPaymentService ticketPaymentService;
  private final SeatReservationService seatReservationService;

  public TicketServiceImpl(
      TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
    this.ticketPaymentService = ticketPaymentService;
    this.seatReservationService = seatReservationService;
  }

  /*
   * Should only have private methods other than the one below.
   *
   * Split solution into two for loops. Though less performant, this aids in readability and
   * reusability.
   */
  @Override
  public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
      throws InvalidPurchaseException {
    validateId(accountId);
    validateTicketRequest(ticketTypeRequests);

    int totalAmountToPay = 0;
    int totalSeats = 0;

    for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
      totalAmountToPay += ticketTypeRequest.getTotalPrice();
      totalSeats += ticketTypeRequest.getTotalSeats();
    }

    ticketPaymentService.makePayment(accountId, totalAmountToPay);
    seatReservationService.reserveSeat(accountId, totalSeats);
  }

  private void validateId(Long accountId) {
    if (accountId < 1) {
      throw new InvalidPurchaseException("Invalid accountId. accountId should be greater than 0.");
    }
  }

  private void validateTicketRequest(TicketTypeRequest... ticketTypeRequests)
      throws InvalidPurchaseException {
    int totalNoOfTickets = 0;
    boolean isPurchasingAdultTicket = false;
    int adultTicketsTotal = 0;
    int infantTicketsTotal = 0;

    for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
      int noOfTickets = ticketTypeRequest.getNoOfTickets();
      TicketTypeRequest.Type ticketType = ticketTypeRequest.getTicketType();

      totalNoOfTickets += noOfTickets;

      if (ticketType == ADULT) {
        isPurchasingAdultTicket = true;
        adultTicketsTotal += noOfTickets;
      } else if (ticketType == INFANT) {
        infantTicketsTotal += noOfTickets;
      }
    }

    if (totalNoOfTickets > 20) {
      throw new InvalidPurchaseException(
          "Invalid purchase tickets request. Total number of tickets must not exceed 20.");
    }

    if (!isPurchasingAdultTicket) {
      throw new InvalidPurchaseException(
          "Invalid purchase tickets request. Infant or child tickets must be purchased with"
              + " at least 1 adult ticket.");
    }

    if (adultTicketsTotal < infantTicketsTotal) {
      throw new InvalidPurchaseException(
          "Invalid purchase tickets request. Infant tickets should be less than or equal to adult"
              + " tickets.");
    }
  }
}
