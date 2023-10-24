package uk.gov.dwp.uc.pairtest.validation;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceValidator {

  private final static int MAX_TICKETS_ALLOWED = 20;

  private final static String INVALID_ACCOUNT_ID_EXCEPTION_MESSAGE = "Invalid account ID supplied. Unauthorised to access this service.";

  private final static String INVALID_ADULT_PURCHASE_EXCEPTION = "Your ticket purchase must contain at least one adult ticket.";

  private final static String EXCEEDED_TICKETS_LIMIT_EXCEPTION = "A maximum of 20 tickets are allowed to be purchased at a time.";

  private final static String MORE_INFANTS_THAN_ADULTS_EXCEPTION = "There must be at least one adult ticket purchased for every infant ticket.";


  public static void validateAccountId(Long accountId) {
    if (accountId == null || checkNullInt(accountId.intValue()) < 1) {
      throw new InvalidPurchaseException(HttpStatus.FORBIDDEN.value(), INVALID_ACCOUNT_ID_EXCEPTION_MESSAGE);
    }
  }

  public static void validateTicketsRequest(Map<TicketTypeRequest.Type, Integer> tickets) {
    var numAdultTickets = checkNullInt(tickets.get(TicketTypeRequest.Type.ADULT));
    if (numAdultTickets < 1) {
      throw new InvalidPurchaseException(HttpStatus.BAD_REQUEST.value(), INVALID_ADULT_PURCHASE_EXCEPTION);
    }
    // an infant is sat on an adult's lap, so there must be at least as many adults as infants.
    var numInfantTickets = checkNullInt(tickets.get(TicketTypeRequest.Type.INFANT));
    if (numInfantTickets > numAdultTickets) {
      throw new InvalidPurchaseException(HttpStatus.BAD_REQUEST.value(), MORE_INFANTS_THAN_ADULTS_EXCEPTION);
    }
    if ((numAdultTickets + numInfantTickets + checkNullInt(tickets.get(TicketTypeRequest.Type.CHILD))) > MAX_TICKETS_ALLOWED) {
      throw new InvalidPurchaseException(HttpStatus.BAD_REQUEST.value(), EXCEEDED_TICKETS_LIMIT_EXCEPTION);
    }
  }

  private static Integer checkNullInt(Integer num) {
    return Optional.ofNullable(num).orElse(0);
  }
}
