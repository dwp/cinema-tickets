/**
 * 
 */
package uk.gov.dwp.uc.pairtest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {
	
	private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;
    
    @Before
    public void setUp() {
        ticketPaymentService = mock(TicketPaymentService.class);
        seatReservationService = mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(ticketPaymentService, seatReservationService);
    }
    
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithNullAccountId() throws InvalidPurchaseException {
    	TicketTypeRequest[] requests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)
            };
        ticketService.purchaseTickets(null, requests); // Pass null account ID
    }
    
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithZeroAccountId() throws InvalidPurchaseException {
    	TicketTypeRequest[] requests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)
            };
        ticketService.purchaseTickets(0L, requests); // Pass zero account ID
    }
    
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithNegativeAccountId() throws InvalidPurchaseException {
    	TicketTypeRequest[] requests = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)
            };
        ticketService.purchaseTickets(-1L, requests); // Pass a negative account ID
    }
    
    @Test
    public void testPurchaseTicketsValid() throws InvalidPurchaseException {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)
        };

        ticketService.purchaseTickets(1L, requests);

        // Assert
        verify(ticketPaymentService).makePayment(eq(1L), eq(40)); // £25 + £15
        verify(seatReservationService).reserveSeat(eq(1L), eq(2)); // 1 adult + 1 child
    }
    
    @Test
    public void testPurchaseTicketsNegativeNoOfTickets() {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -1)
        };
        
        try {
            ticketService.purchaseTickets(1L, requests);
            Assert.fail("Expected InvalidPurchaseException to be thrown");
        } catch (InvalidPurchaseException e) {
            Assert.assertEquals("Ticket number cannot be negative for ticket type: ADULT", e.getMessage());
        }
        
    }
    
    @Test
    public void testPurchaseTicketsMoreThanMaxTicketsAllowed() {
        TicketTypeRequest[] requests = new TicketTypeRequest[26];
        for (int i = 0; i < 25; i++) {
            requests[i] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        }
        requests[25] = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1); // 26 tickets

        try {
            ticketService.purchaseTickets(1L, requests);
            Assert.fail("Expected InvalidPurchaseException to be thrown");
        } catch (InvalidPurchaseException e) {
            Assert.assertEquals("Cannot purchase more than 25 tickets.", e.getMessage());
        }

    }
    
    @Test
    public void testPurchaseTicketsWithoutAdult() {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)
        };

        try {
            ticketService.purchaseTickets(1L, requests);
            Assert.fail("Expected InvalidPurchaseException to be thrown");
        } catch (InvalidPurchaseException e) {
            Assert.assertEquals("Child or Infant tickets cannot be purchased without an Adult ticket.", e.getMessage());
        }
    }
    
    @Test
    public void testPurchaseTicketsWithoutAdultForChildAndInfant() {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)
        };

        try {
            ticketService.purchaseTickets(1L, requests);
            Assert.fail("Expected InvalidPurchaseException to be thrown");
        } catch (InvalidPurchaseException e) {
            Assert.assertEquals("Child or Infant tickets cannot be purchased without an Adult ticket.", e.getMessage());
        }
    }
    
    @Test
    public void testPurchaseTicketsWithValidTickets() throws InvalidPurchaseException {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        };

        ticketService.purchaseTickets(1L, requests);

        verify(ticketPaymentService).makePayment(eq(1L), eq(55)); // £25 + 2 * £15
        verify(seatReservationService).reserveSeat(eq(1L), eq(3)); // 1 adult + 2 children
    }

    @Test
    public void testPurchaseTicketsWithMultipleAdultsChildrenAndInfant() throws InvalidPurchaseException {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2)
        };

        ticketService.purchaseTickets(1L, requests);

        verify(ticketPaymentService).makePayment(eq(1L), eq(95)); // 2 * £25 + 3 * £15
        verify(seatReservationService).reserveSeat(eq(1L), eq(5)); // 2 adults + 3 children
    }
    
    @Test
    public void testPurchaseTicketsWithInfantsOnly() {
        TicketTypeRequest[] requests = {
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2)
        };

        try {
            ticketService.purchaseTickets(1L, requests);
            Assert.fail("Expected InvalidPurchaseException to be thrown");
        } catch (InvalidPurchaseException e) {
            Assert.assertEquals("Child or Infant tickets cannot be purchased without an Adult ticket.", e.getMessage());
        }
    }

}
