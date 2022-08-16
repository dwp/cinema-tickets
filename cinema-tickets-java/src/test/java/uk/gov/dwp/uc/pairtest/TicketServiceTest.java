package uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

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
}
