import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';

/*
Provide a working implementation of a `TicketService` that:
- Considers the above objective, business rules, constraints & assumptions.

- Calculates the correct amount for the requested tickets and makes a payment request to the `TicketPaymentService`.  
- must be perfect request as tickets are purchased

- Calculates the correct no of seats to reserve and makes a seat reservation request to the `SeatReservationService`.
- must be perfect request as seats are booked  

- Rejects any invalid ticket purchase requests. It is up to you to identify what should be deemed as an invalid purchase request.

//ticketTypeRequest ARG = object {ADULT: NUM, CHILD, NUM, INFANT: NUM}
*/

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets(accountId, ...ticketTypeRequests) {
    // throws InvalidPurchaseException

    /*Plan:
    Should check inputs using private methods and then make a request to 
    - TicketPaymentService
    - SatReservtionService
    */
  }

  
}


