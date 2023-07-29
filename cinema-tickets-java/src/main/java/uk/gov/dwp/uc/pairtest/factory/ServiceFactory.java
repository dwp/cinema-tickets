/**
 * PROJECT NAME
 *    cinema-tickets-java
 *
 * FILE NAME
 *    ServiceFactory.java
 *
 * CREATED ON
 *    Jul 29, 2023 4:01:09 PM
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
package uk.gov.dwp.uc.pairtest.factory;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;

/**
 * <p>
 * Factory to create required service objects.
 * </p>
 *
 * @since   1.0.0
 *
 * @author  Hansaka Weerasingha
 *
 * @version $Id$
 */
public class ServiceFactory {

    private TicketPaymentService paymentService;
    private SeatReservationService reservationService;

    /**
     * Create an instance of {@link TicketPaymentService}.
     *
     * @return A {@link TicketPaymentService}.
     */
    public TicketPaymentService getPaymentServiceInstance() {
        if (paymentService == null) {
            paymentService = new TicketPaymentServiceImpl();
        }

        return paymentService;
    }

    /**
     * Create an instance of {@link SeatReservationService}.
     *
     * @return A {@link SeatReservationService}.
     */
    public SeatReservationService getReservationServiceInstance() {
        if (reservationService == null) {
            reservationService = new SeatReservationServiceImpl();
        }

        return reservationService;
    }

}
