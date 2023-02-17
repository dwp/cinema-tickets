package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.CHILD;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.INFANT;

class TicketTypeRequestTest {

  @Test
  void getNoOfTickets() {
    TicketTypeRequest underTest = new TicketTypeRequest(INFANT, 10);

    assertEquals(10, underTest.getNoOfTickets());
  }

  @Test
  void getTicketType() {
    TicketTypeRequest underTest = new TicketTypeRequest(CHILD, 10);

    assertEquals(CHILD, underTest.getTicketType());
  }

  @ParameterizedTest
  @CsvSource({
    "INFANT, 0, 0",
    "INFANT, 5, 0",
    "CHILD, 0, 0",
    "CHILD, 5, 50",
    "ADULT, 0, 0",
    "ADULT, 10, 200"
  })
  void getTotalPrice(TicketTypeRequest.Type type, int noOfTickets, int expectedPrice) {
    TicketTypeRequest underTest = new TicketTypeRequest(type, noOfTickets);

    assertEquals(expectedPrice, underTest.getTotalPrice());
  }

  @ParameterizedTest
  @CsvSource({
    "INFANT, 0, 0",
    "INFANT, 5, 0",
    "CHILD, 0, 0",
    "CHILD, 5, 5",
    "ADULT, 0, 0",
    "ADULT, 10, 10"
  })
  void getTotalSeats(TicketTypeRequest.Type type, int noOfTickets, int expectedTotalSeats) {
    TicketTypeRequest underTest = new TicketTypeRequest(type, noOfTickets);

    assertEquals(expectedTotalSeats, underTest.getTotalSeats());
  }
}
