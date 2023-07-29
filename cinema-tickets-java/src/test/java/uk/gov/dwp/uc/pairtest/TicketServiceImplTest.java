/**
 * PROJECT NAME
 *    cinema-tickets-java
 *
 * FILE NAME
 *    TicketServiceImplTest.java
 *
 * CREATED ON
 *    Jul 29, 2023 1:37:08 PM
 *
 * COPYRIGHT
 *    Copyright 2022 © Hansaka Weerasingha. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.dwp.uc.pairtest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.factory.ServiceFactory;

/**
 * <p>
 * Unit test for {@link TicketServiceImpl}.
 * </p>
 *
 * @since   1.0.0
 *
 * @author  Hansaka Weerasingha
 *
 * @version $Id$
 */
public class TicketServiceImplTest {

    private final ServiceFactory serviceFactory = new ServiceFactory();
    private final TicketService ticketService = new TicketServiceImpl(serviceFactory.getPaymentServiceInstance(), serviceFactory.getReservationServiceInstance());

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Success scenario.
     * </p>
     */
    @Test
    public void testPurchaseTicketsSuccess() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 1);
        final TicketTypeRequest ticketTypeRequest2 = createTicketTypeRequest(Type.CHILD, 1);
        final TicketTypeRequest ticketTypeRequest3 = createTicketTypeRequest(Type.INFANT, 1);
        // Given
        ticketService.purchaseTickets(accountId, ticketTypeRequest1, ticketTypeRequest2, ticketTypeRequest3);
        // Then
        assertTrue("Purchase success!", true);
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Only Adult.
     * </p>
     */
    @Test
    public void testPurchaseTicketsFailOnlyAdultTicket() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 1);
        // Given
        ticketService.purchaseTickets(accountId, ticketTypeRequest1);
        // Then
        assertTrue("Purchase success!", true);
    }


    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Only child No Adult.
     * </p>
     */
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsFailOnlyChildTicket() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = null;
        final TicketTypeRequest ticketTypeRequest2 = createTicketTypeRequest(Type.CHILD, 1);
        // Given
        ticketService.purchaseTickets(accountId, ticketTypeRequest1, ticketTypeRequest2);
        // Then
        // Exception expected
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Exceed max ticket count.
     * </p>
     */
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsFailMaxTicketCount() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 10);
        final TicketTypeRequest ticketTypeRequest2 = createTicketTypeRequest(Type.CHILD, 6);
        final TicketTypeRequest ticketTypeRequest3 = createTicketTypeRequest(Type.INFANT, 6);
        // Given
        ticketService.purchaseTickets(accountId, ticketTypeRequest1, ticketTypeRequest2, ticketTypeRequest3);
        // Then
        // Exception expected
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Invalid account.
     * </p>
     */
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsFailInvalidAccount() {
        // When
        final Long accountId = -10l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 1);
        // Given
        ticketService.purchaseTickets(accountId, ticketTypeRequest1);
        // Then
        // Exception expected
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Empty ticket type requests.
     * </p>
     */
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsFailEmptyRequests() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = null;
        // Given
        ticketService.purchaseTickets(accountId, ticketTypeRequest1);
        // Then
        // Exception expected
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Null ticket type requests.
     * </p>
     */
    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsFailNullRequests() {
        // When
        final Long accountId = 1000l;
        // Given
        ticketService.purchaseTickets(accountId, null);
        // Then
        // Exception expected
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Test invocations.
     * </p>
     */
    @Test
    public void testPurchaseTicketsVerifyInvocations() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 1);
        // Given
        final TicketService spyTicketService = Mockito.spy(ticketService);
        spyTicketService.purchaseTickets(accountId, ticketTypeRequest1);
        // Then
        Mockito.verify(spyTicketService, Mockito.times(1)).purchaseTickets(accountId, ticketTypeRequest1);
    }

    /**
     * Test method for {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     *  Test failure scenario - Third party service error.
     * </p>
     */
    @Test
    public void testPurchaseTicketsThirdPartyServiceError() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 1);

        final TicketPaymentService mockPaymentService = Mockito.mock(TicketPaymentServiceImpl.class);
        Mockito.doThrow(RuntimeException.class).when(mockPaymentService).makePayment(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt());

        final SeatReservationService mockReservationService = Mockito.mock(SeatReservationServiceImpl.class);
        Mockito.doCallRealMethod().when(mockReservationService).reserveSeat(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt());

        // Given
        final TicketService spyTicketService = Mockito.spy(new TicketServiceImpl(mockPaymentService, mockReservationService));
        spyTicketService.purchaseTickets(accountId, ticketTypeRequest1);

        // Then
        Mockito.verify(spyTicketService, Mockito.times(1)).purchaseTickets(accountId, ticketTypeRequest1);
        Mockito.verify(mockPaymentService, Mockito.times(1)).makePayment(accountId, 20);
    }

    /**
     * Test method for
     * {@link uk.gov.dwp.uc.pairtest.TicketServiceImpl#purchaseTickets(java.lang.Long, uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest[])}.
     * <p>
     * Test failure scenario - Verify ticket count and amount.
     * </p>
     */
    @Test
    public void testPurchaseTicketsVerifyTicketCountAndAmount() {
        // When
        final Long accountId = 1000l;
        final TicketTypeRequest ticketTypeRequest1 = createTicketTypeRequest(Type.ADULT, 2);
        final TicketTypeRequest ticketTypeRequest2 = createTicketTypeRequest(Type.CHILD, 3);
        final TicketTypeRequest ticketTypeRequest3 = createTicketTypeRequest(Type.INFANT, 2);

        final TicketPaymentService mockPaymentService = Mockito.mock(TicketPaymentServiceImpl.class);
        Mockito.doCallRealMethod().when(mockPaymentService).makePayment(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt());

        final SeatReservationService mockReservationService = Mockito.mock(SeatReservationServiceImpl.class);
        Mockito.doCallRealMethod().when(mockReservationService).reserveSeat(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt());

        // Given
        final TicketService spyTicketService = Mockito.spy(new TicketServiceImpl(mockPaymentService, mockReservationService));
        spyTicketService.purchaseTickets(accountId, ticketTypeRequest1, ticketTypeRequest2, ticketTypeRequest3);

        // Then
        Mockito.verify(spyTicketService, Mockito.times(1)).purchaseTickets(accountId, ticketTypeRequest1, ticketTypeRequest2, ticketTypeRequest3);
        Mockito.verify(mockPaymentService, Mockito.times(1)).makePayment(accountId, 70);
        Mockito.verify(mockReservationService, Mockito.times(1)).reserveSeat(accountId, 5);
    }

    /**
     * Crate a sample ticket type request.
     *
     * @param  ticketType The ticket type
     * @param  qty        Number of tickets required
     *
     * @return            A sample ticket type request
     */
    private TicketTypeRequest createTicketTypeRequest(final Type ticketType, final int qty) {
        final TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(ticketType, qty);
        return ticketTypeRequest;
    }

}
