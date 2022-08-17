package uk.gov.dwp.uc.pairtest;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.AccountNumberException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.TicketServiceValidator;

public class TicketServiceImpl implements TicketService {

  private final TicketPaymentService ticketPaymentService;
  private final SeatReservationService seatReservationService;
  private static final Logger LOGGER = Logger.getLogger( TicketServiceImpl.class.getName() );

  public TicketServiceImpl(TicketPaymentService ticketPaymentService,
      SeatReservationService seatReservationService) {
    this.ticketPaymentService = ticketPaymentService;
    this.seatReservationService = seatReservationService;
  }

  @Override
  public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
      throws InvalidPurchaseException {
    validateRequest(accountId, ticketTypeRequests);

    int totalPaymentAmount = calculateTotalPaymentAmount(ticketTypeRequests);
    LOGGER.log(Level.INFO, String.format("Making request to TicketPaymentService to take payment of £%s", totalPaymentAmount));
    ticketPaymentService.makePayment(accountId, totalPaymentAmount);

    int numberOfSeatsToReserve = calculateTotalNumberOfSeatsToReserve(ticketTypeRequests);
    LOGGER.log(Level.INFO, String.format("Making request to SeatReservationService to reserve %s seat(s)",numberOfSeatsToReserve));
    seatReservationService.reserveSeat(accountId, numberOfSeatsToReserve);
  }

  private void validateRequest(Long accountId, TicketTypeRequest... ticketTypeRequests) {
    if (accountId <= 0) {
      throw new AccountNumberException();
    }

    TicketServiceValidator.validateRequest(ticketTypeRequests);
  }

  private int calculateTotalNumberOfSeatsToReserve(TicketTypeRequest... ticketTypeRequests) {
    return Arrays.stream(ticketTypeRequests)
        .mapToInt(TicketTypeRequest::getTotalSeatsToReserve)
        .sum();
  }

  private int calculateTotalPaymentAmount(TicketTypeRequest... ticketTypeRequests) {
    return Arrays.stream(ticketTypeRequests)
        .mapToInt(TicketTypeRequest::getTotalPrice)
        .sum();
  }
}
