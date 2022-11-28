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

  /**
   * Array of Ticket Request objects for additional processing.
   * @type {[] Class TicketTypeRequest}
   */
  #allTicketRequestObjects = [];
  /**
   * Boolean value to represent if ADULT type tickets present in total Ticket requests.
   * @type {boolean}
   */
  
  /**
   * Boolean value to represent if CHILD and/or INFANT type tickets present in total Ticket requests.
   * @type {boolean}
   */
  

  /**
   * Number value to count total number of tickets purchased.
   * @type {number}
   */
  #totalTicketCount = 0;

  //methods

  /**
   * Throws InvalidPurchaseError if accountId is not valid.
   *
   * @param {number} accountId id to check.
   */
  #checkId(accountId) {
    if (accountId < 1) {
      throw new InvalidPurchaseException(
        "accountIdError",
        "accountId must be greater than 0."
      );
    }
  }

  /**
   * Throws InvalidPurchaseException if CHILD and/or INFANT tickets purchased without ADULT ticket type.
   *
   * @param {[]Class TicketTypeRequest} arrTicketRequests The ticketTypeRequest object to check
   */
  #checkAdultsPresentForChildrenInfants(arrTicketRequests) {
    let adultsPresent = false;
    let childrenOrInfantsPresent = false;
    arrTicketRequests.forEach((request) => {
      if (request.getTicketType() === "ADULT" && request.getNoOfTickets() > 0) {
        //console.log(request.getTicketType(),request.getNoOfTickets())
        adultsPresent = true;
      }
      if (request.getTicketType() && request.getNoOfTickets() > 0) {
        childrenOrInfantsPresent = true;
      }
      if (request.getTicketType() && request.getNoOfTickets() > 0) {
        childrenOrInfantsPresent = true;
      }
    });
    console.log(adultsPresent, childrenOrInfantsPresent)
    if (
      adultsPresent === false &&
      childrenOrInfantsPresent === true
    ) {
      throw new InvalidPurchaseException(
        "invalidNumberOfAdultTicketsError",
        "Cannot purchase CHILD or INFANT tickets without purchasing ADULT tickets"
      );
    }
  }

  
   /**
   * Throws InvalidPurchaseException if number of tickets purchased is not between 0 exclusive and 20 inclusive.
   *
   * @param {[]Class TicketTypeRequest} arrTicketRequests The ticketTypeRequest object to check
   */
  #countTickets(arrTicketRequests) {
    let totalNoOfTickets = 0;
    arrTicketRequests.forEach((request) => {
      const noOfTickets = request.getNoOfTickets();
      totalNoOfTickets += noOfTickets;
    });
    if (totalNoOfTickets< 1) {
      throw new InvalidPurchaseException(
        "invalidNumberOfTicketsError",
        "Cannot purchase 0 tickets."
      );
    } else if (totalNoOfTickets > 20) {
      throw new InvalidPurchaseException(
        "invalidNumberOfTicketsError",
        "Cannot purchase more than 20 tickets."
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
      this.#allTicketRequestObjects.push(ticketRequest);
    }

    //throws InvalidPurchaseExceptions if adults not present for infants or children
    this.#checkAdultsPresentForChildrenInfants(this.#allTicketRequestObjects);

    //throws InvalidPurchaseExceptions if total number of tickets is not between 0 exclusive and 20 inclusive.
    this.#countTickets(this.#allTicketRequestObjects);
    /*Plan:
    - generate TicketTypeRequests -> merge -> - SatReservtionService
    - calc total payment -> TicketPaymentService
    */
  }
}
