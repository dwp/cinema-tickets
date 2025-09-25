//Jesus
package uk.gov.dwp.uc.pairtest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
class TicketServiceImplIntegrationTest {

    @Autowired
    private TicketServiceImpl ticketService;

    @MockBean
    private TicketPaymentService paymentService;

    @MockBean
    private SeatReservationService seatService;

    @Test
    void purchaseTickets_integration_shouldWorkWithSpringContext() throws InvalidPurchaseException {
        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        ticketService.purchaseTickets(accountId, adult, child);

        // Business logic: 2 adults (25*2=50) + 1 child (15*1=15) = 65, seats: 3
        verify(paymentService, times(1)).makePayment(accountId, 65);
        verify(seatService, times(1)).reserveSeat(accountId, 3);
    }

    @Test
    void shouldThrowExceptionWhenNoAdultWithChildIntegration() {
        Long accountId = 1L;
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        try {
            ticketService.purchaseTickets(accountId, child);
        } catch (InvalidPurchaseException e) {
            //assert e.getMessage().contains("Adult");
            assert containsIgnoreCase(e.getMessage(), "Adult");
        }
    }
    
    public static boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }
}