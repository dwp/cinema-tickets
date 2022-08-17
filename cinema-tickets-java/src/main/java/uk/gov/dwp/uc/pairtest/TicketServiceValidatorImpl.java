package uk.gov.dwp.uc.pairtest;

import java.util.HashMap;
import java.util.Map;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceValidatorImpl {

  public static void validateRequest(TicketTypeRequest... ticketTypeRequests) {
    Map<Type, Integer> mapOfTicketsPerType = getMapOfTicketsPerType(ticketTypeRequests);

    int numberOfAdultTickets = mapOfTicketsPerType.get(Type.ADULT);
    int numberOfChildTickets = mapOfTicketsPerType.get(Type.CHILD);
    int numberOfInfantTickets = mapOfTicketsPerType.get(Type.INFANT);
    int totalNumberOfTickets = numberOfAdultTickets + numberOfChildTickets + numberOfInfantTickets;

    if ((numberOfChildTickets > 0 || numberOfInfantTickets > 0)
        && numberOfAdultTickets == 0) {
      throw new InvalidPurchaseException("ERROR: At least one adult ticket is required when purchasing a child/infant ticket");
    } else if (totalNumberOfTickets > 20) {
      throw new InvalidPurchaseException("ERROR: Only 20 tickets can be requested at once");
    } else if (numberOfInfantTickets > numberOfAdultTickets) {
      throw new InvalidPurchaseException(
          "ERROR: Each infant ticket must be accompanied by an adult ticket");
    }
  }

  private static Map<Type, Integer> getMapOfTicketsPerType(TicketTypeRequest... ticketTypeRequests) {
    Map<Type, Integer> mapOfTicketsPerType = new HashMap<>(
        Map.of(Type.ADULT, 0, Type.CHILD, 0, Type.INFANT, 0));

    for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
      int numberOfSeats = mapOfTicketsPerType.get(ticketTypeRequest.getTicketType());
      Type ticketType = ticketTypeRequest.getTicketType();
      mapOfTicketsPerType.put(ticketType, numberOfSeats + ticketTypeRequest.getNoOfTickets());
    }

    return mapOfTicketsPerType;
  }
}
