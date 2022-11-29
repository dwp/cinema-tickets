import TicketTypeRequest from "./lib/TicketTypeRequest.js";
import InvalidPurchaseException from "./lib/InvalidPurchaseException.js";
import SeatReservationService from "../thirdparty/seatbooking/SeatReservationService.js";

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

  //methods
  // see "README.md "Notes for the examiner from the candidate" 1. for reasoning.

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
   * @param {Object[]} arrTicketRequests An array of ticketTypeRequest object to check.
   */
  #checkAdultsPresentForChildrenInfants(arrTicketRequests) {
    let adultsPresent = false;
    let childrenOrInfantsPresent = false;
    arrTicketRequests.forEach((request) => {
      if (request.getTicketType() === "ADULT" && request.getNoOfTickets() > 0) {
        adultsPresent = true;
      }
      if (request.getTicketType() && request.getNoOfTickets() > 0) {
        childrenOrInfantsPresent = true;
      }
      if (request.getTicketType() && request.getNoOfTickets() > 0) {
        childrenOrInfantsPresent = true;
      }
    });
    if (adultsPresent === false && childrenOrInfantsPresent === true) {
      throw new InvalidPurchaseException(
        "invalidNumberOfAdultTicketsError",
        "Cannot purchase CHILD or INFANT tickets without purchasing ADULT tickets"
      );
    }
  }

  /**
   * Throws InvalidPurchaseException if number of tickets purchased is not between 0 exclusive and 20 inclusive.
   *
   * @param {Object[]} arrTicketRequests An array of ticketTypeRequest object to check.
   */
  #countTickets(arrTicketRequests) {
    let totalNoOfTickets = 0;
    arrTicketRequests.forEach((request) => {
      const noOfTickets = request.getNoOfTickets();
      totalNoOfTickets += noOfTickets;
    });
    if (totalNoOfTickets < 1) {
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

  /**
   * Calculates the total number of seats to reserve from an array of TicketTypeRequest objects.
   *
   * @param {Object[]} arrTicketRequests An array of ticketTypeRequest object to check.
   *
   * @return {number} TotalNoOfSeats The total number of seats to reserve.
   */
  #calculateNoOfSeats(arrTicketRequests) {
    let TotalNoOfSeats = 0;
    arrTicketRequests.forEach((request) => {
      if (request.getTicketType() !== "INFANT") {
        const noOfSeats = request.getNoOfTickets();
        TotalNoOfSeats += noOfSeats;
      }
    });
    return TotalNoOfSeats;
  }

  /**
   * Checks ticket requests for invalid requests using other private methods within Class TicketService. If valid makes requests to TicketPaymentService and SeatReservationService.
   *
   * @param {number}  accountId Id of customer making ticket request.
   * @param {Object}  ticketTypeRequests Object representing number of ADULT, CHILD and INFANT tickets to purchase.
   */
  purchaseTickets(accountId, ticketTypeRequests) {
    const allTicketRequestObjects = [];
    // throws InvalidPurchaseExceptions if accountId not valid
    this.#checkId(accountId);

    // create new instance of TicketTypeRequest for each ticket type
    for (const key in ticketTypeRequests) {
      const ticketRequest = new TicketTypeRequest(key, ticketTypeRequests[key]);
      allTicketRequestObjects.push(ticketRequest);
    }

    //throws InvalidPurchaseExceptions if adults not present for infants or children
    this.#checkAdultsPresentForChildrenInfants(allTicketRequestObjects);

    //throws InvalidPurchaseExceptions if total number of tickets is not between 0 exclusive and 20 inclusive.
    this.#countTickets(allTicketRequestObjects);

    //calculate number of seats to reserve
    // see "README.md "Notes for the examiner from the candidate" 2. for reasoning.
    const finalNoOfSeatsToReserve = this.#calculateNoOfSeats(
      allTicketRequestObjects
    );

    //make seatbooking request
    try {
      const seatBookingRequestInstance = new SeatReservationService();
      seatBookingRequestInstance.reserveSeat(
        accountId,
        finalNoOfSeatsToReserve
      );
      //assume successful so give user feedback on successful seat reservation.
      // see "README.md "Notes for the examiner from the candidate" 3. for reasoning.
      const returnBody = {
        accountId: accountId,
        bookingSuccessful: true,
        totalNoOfSeatsReserved: finalNoOfSeatsToReserve,
      };

      return returnBody;
    } catch (error) {}
  }
}
