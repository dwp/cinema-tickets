import TicketTypeRequest from "./lib/TicketTypeRequest.js";
import InvalidPurchaseException from "./lib/InvalidPurchaseException.js";

/*
Provide a working implementation of a `TicketService` that:
- Considers the above objective, business rules, constraints & assumptions.

- Calculates the correct amount for the requested tickets and makes a payment request to the `TicketPaymentService`.  
- must be perfect request as tickets are purchased

- Calculates the correct no of seats to reserve and makes a seat reservation request to the `SeatReservationService`.
- must be perfect request as seats are booked  

- Rejects any invalid ticket purchase requests. It is up to you to identify what should be deemed as an invalid purchase request.

//ticketTypeRequest ARG = object {ADULT: NUM, CHILD, NUM, INFANT: NUM}

# Predefined functions

TicketPaymentService.makePayment(accountID: number, totalCostToPay: Number/interger)
SeatReservationService.reserveSeat(accountId: integer, totalSeatsToAllocate: integer)

CLASS TicketTypeRequest (ticketType: string, noOfTickets: number)
CLASS InvalidPurchaseExceptions extends Errors define custom errors
*/

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */
  //variables
  #AllTicketRequestObjects = [];
  #AdultsPresent = false;
  #ChildrenOrInfantsPresent = false;

  //methods
  #checkId(accountId) {
    if (accountId < 1) {
      throw new InvalidPurchaseException(
        "accountIdError",
        "accountId must be greater than 0."
      );
    }
  }

  #checkAdultsPresentForChildrenInfants(arrTicketRequests) {
    //throw err if children found and adults not found
    arrTicketRequests.forEach((request) => {
      if (request.getTicketType() === "ADULT" && request.getNoOfTickets() > 0) {
        console.log(request.getTicketType(),request.getNoOfTickets())
        this.#AdultsPresent = true;
      }
      if (request.getTicketType() && request.getNoOfTickets() > 0) {
        this.#ChildrenOrInfantsPresent = true;
      } 
      if (request.getTicketType() && request.getNoOfTickets() > 0) {
        this.#ChildrenOrInfantsPresent = true;
      }
    });
    if (
      this.#AdultsPresent === false &&
      this.#ChildrenOrInfantsPresent === true
    ) {
      throw new InvalidPurchaseException(
        "invalidNumberOfAdultTicketsError",
        "Cannot purchase CHILD or INFANT tickets without purchasing ADULT tickets"
      );
    }
  }

  purchaseTickets(accountId, ticketTypeRequests) {
    // throws InvalidPurchaseExceptions if accountId not valid

    this.#checkId(accountId);
    //console.log("ticketTypeRequests", ticketTypeRequests);

    // create new instance of TicketTypeRequest for each ticket type
    for (const key in ticketTypeRequests) {
      const ticketRequest = new TicketTypeRequest(key, ticketTypeRequests[key]);
      this.#AllTicketRequestObjects.push(ticketRequest);
      // console.log(ticketRequest.getTicketType(), ticketRequest.getNoOfTickets())
    }

    //throws InvalidPurchaseExceptionsif adults not present for infants or children
    this.#checkAdultsPresentForChildrenInfants(this.#AllTicketRequestObjects);

    /*Plan:
    - generate TicketTypeRequests -> merge -> - SatReservtionService
    - calc total payment -> TicketPaymentService
    
    */
  }
}
