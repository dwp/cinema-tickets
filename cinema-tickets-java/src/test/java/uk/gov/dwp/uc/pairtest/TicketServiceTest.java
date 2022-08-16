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
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

  // Mocked implementations of the third party service
  @Mock
  private TicketPaymentService ticketPaymentService;

  // create instance of TicketService with mocked services injected
  @InjectMocks
  private TicketServiceImpl ticketService;

  @Test
  public void testOneAdultRequestsPaymentOf20() {
    TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 1);
    ticketService.purchaseTickets(1L, request);
    verify(ticketPaymentService, times(1)).makePayment(1L, 20);
  }

  @Test
  public void testOneChildWithoutAdultCannotMakeARequest() {
    TicketTypeRequest request = new TicketTypeRequest(Type.CHILD, 1);

    Exception exception = assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, request));

    String expectedMessage = "ERROR: At least one adult ticket is required when purchasing a child/infant ticket";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testOneInfantWithoutAdultCannotMakeARequest() {
    TicketTypeRequest request = new TicketTypeRequest(Type.INFANT, 1);

    Exception exception = assertThrows(InvalidPurchaseException.class, () ->
        ticketService.purchaseTickets(1L, request));

    String expectedMessage = "ERROR: At least one adult ticket is required when purchasing a child/infant ticket";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
