package uk.gov.dwp.uc.pairtest.validation;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceValidatorTest {

  private HashMap<TicketTypeRequest.Type, Integer> tickets;

  @BeforeEach
  public void setUp() {
    tickets = new HashMap<>();
  }

  @Test
  public void testValidAccountId() {
    // test invalid account id
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateAccountId(null));
    //test invalid account id (extreme)
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateAccountId(0L));

    //test valid account id (extreme)
    assertDoesNotThrow(() -> TicketServiceValidator.validateAccountId(1L));

    //test valid account id
    assertDoesNotThrow(() -> TicketServiceValidator.validateAccountId(22L));
  }

  @Test
  public void testValidNumAdultTicketsPurchased() {

    // test invalid num adult tickets purchased
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));
    tickets.put(TicketTypeRequest.Type.ADULT, 0);
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));
    tickets.put(TicketTypeRequest.Type.CHILD, 3);
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));

    // test valid purchase
    tickets.put(TicketTypeRequest.Type.ADULT, 1);
    assertDoesNotThrow(() -> TicketServiceValidator.validateTicketsRequest(tickets));
  }

  @Test
  void testValidNumTicketsPurchased() {
    //test invalid num tickets purchased
    tickets.put(TicketTypeRequest.Type.ADULT, 25);
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));
    tickets.put(TicketTypeRequest.Type.ADULT, 21);
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));

    //test valid num tickets purchased
    tickets.put(TicketTypeRequest.Type.ADULT, 20);
    assertDoesNotThrow(() -> TicketServiceValidator.validateTicketsRequest(tickets));
    tickets.put(TicketTypeRequest.Type.ADULT, 5);
    assertDoesNotThrow(() -> TicketServiceValidator.validateTicketsRequest(tickets));
  }

  @Test
  void testValidAdultAndInfantTicketsPurchased() {
    // test invalid num adult and infant tickets purchased
    tickets.put(TicketTypeRequest.Type.INFANT, 3);
    tickets.put(TicketTypeRequest.Type.ADULT, 0);
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));
    tickets.replace(TicketTypeRequest.Type.ADULT, 2);
    assertThrows(InvalidPurchaseException.class, () -> TicketServiceValidator.validateTicketsRequest(tickets));

    // test valid purchase (same
    tickets.replace(TicketTypeRequest.Type.ADULT, 3);
    assertDoesNotThrow(() -> TicketServiceValidator.validateTicketsRequest(tickets));
  }
}