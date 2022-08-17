package uk.gov.dwp.uc.pairtest;

import static org.junit.Assert.assertThrows;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InfantAdultTicketsException;
import uk.gov.dwp.uc.pairtest.exception.NoAdultTicketException;
import uk.gov.dwp.uc.pairtest.exception.TooManyTicketsException;

public class TicketServiceValidatorTest {

  @Test
  public void testOneChildWithoutAdultCannotMakeARequest() {
    TicketTypeRequest request = new TicketTypeRequest(Type.CHILD, 1);

    assertThrows(NoAdultTicketException.class, () ->
        TicketServiceValidator.validateRequest(request));
  }

  @Test
  public void testOneInfantWithoutAdultCannotMakeARequest() {
    TicketTypeRequest request = new TicketTypeRequest(Type.INFANT, 1);

    assertThrows(NoAdultTicketException.class, () ->
        TicketServiceValidator.validateRequest(request));
  }

  @Test
  public void testMoreInfantsThanAdultsThrowsException() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 1);
    TicketTypeRequest infantRequest = new TicketTypeRequest(Type.INFANT, 2);

    assertThrows(InfantAdultTicketsException.class, () ->
        TicketServiceValidator.validateRequest(adultRequest, infantRequest));
  }

  @Test
  public void testMoreThan20TicketsCannotBeRequestedAtOnce() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 21);

    assertThrows(TooManyTicketsException.class, () ->
        TicketServiceValidator.validateRequest(request));
  }

  @Test
  public void testMoreThan20TicketsCannotBeRequestedAtOnceDifferentTicketTypes() {
    TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 18);
    TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 3);

    assertThrows(TooManyTicketsException.class, () ->
        TicketServiceValidator.validateRequest(adultRequest, childRequest));
  }
}
