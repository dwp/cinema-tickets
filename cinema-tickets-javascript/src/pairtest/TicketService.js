import TicketTypeRequest from "./lib/TicketTypeRequest.js";
import InvalidPurchaseException from "./lib/InvalidPurchaseException.js";
import SeatReservationService from "../thirdparty/seatbooking/SeatReservationService.js";
import TicketPaymentService from "../thirdparty/paymentgateway/TicketPaymentService.js";

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */
  
  //@typeDefs

  /**
 * @typedef {Object} bookingObject
 * @property {number} accountId id of account.
 * @property {boolean} bookingSuccessful Boolean value representing a successful booking request.
 * @property {number} totalNoOfSeatsReserved The total number of seats to reserve.
 * @property {number} totalTicketsCost The total payment for all tickets purchased.
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
   * @param {Object[]} allTicketRequestObjects An array of ticketTypeRequest object to check.
   */
  #checkAdultsPresentForChildrenInfants(allTicketRequestObjects) {
    let adultsPresent = false;
    let childrenOrInfantsPresent = false;
    allTicketRequestObjects.forEach((request) => {
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
   * @param {Object[]} allTicketRequestObjects An array of ticketTypeRequest object to check.
   */
  #countTickets(allTicketRequestObjects) {
    let totalNoOfTickets = 0;
    allTicketRequestObjects.forEach((request) => {
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
   * @param {Object[]} allTicketRequestObjects An array of ticketTypeRequest object to check.
   *
   * @return {number} TotalNoOfSeats The total number of seats to reserve.
   */
  #calculateNoOfSeats(allTicketRequestObjects) {
    let totalNoOfSeats = 0;
    allTicketRequestObjects.forEach((request) => {
      if (request.getTicketType() !== "INFANT") {
        const noOfSeats = request.getNoOfTickets();
        totalNoOfSeats += noOfSeats;
      }
    });
    return totalNoOfSeats;
  }

  /**
   * Calculates the total payment for all tickets purchased from an array of TicketTypeRequest objects.
   *
   * @param {Object[]} allTicketRequestObjects An array of ticketTypeRequest object to check.
   *
   * @return {number} totalAmountToPay The total payment for all tickets purchased.
   */
  #calculateTotalAmountToPay(allTicketRequestObjects) {
    let totalAmountToPay = 0;
    allTicketRequestObjects.forEach((request) => {
      if (request.getTicketType() === "ADULT") {
        const amountToPayPerType = request.getNoOfTickets() * 20;
        totalAmountToPay += amountToPayPerType;
      } else if (request.getTicketType() === "CHILD") {
        const amountToPayPerType = request.getNoOfTickets() * 10;
        totalAmountToPay += amountToPayPerType;
      } else if (request.getTicketType() === "INFANT") {
        const amountToPayPerType = request.getNoOfTickets() * 0;
        totalAmountToPay += amountToPayPerType;
      }
    });
    return totalAmountToPay;
  }

  /**
   * Makes seat reservation requests to SeatReservationService.reserveSeat() and payment requests to TicketPaymentService.makePayment().
   *
   * @param {number}  accountId id of customer making ticket request.
   * @param {number}  finalNoOfSeatsToReserve The total number of seats to reserve.
   * @param {number}  totalAmountToPay The total payment for all tickets purchased.
   * 
   * @return {Object} bookingObject Object modelling the successful booking requests.
   */
  #makeBookingRequests(accountId, finalNoOfSeatsToReserve, totalAmountToPay) {
    const seatBookingRequestInstance = new SeatReservationService();
    seatBookingRequestInstance.reserveSeat(accountId, finalNoOfSeatsToReserve);

    const ticketPaymentRequestInstance = new TicketPaymentService();
    ticketPaymentRequestInstance.makePayment(accountId, totalAmountToPay);

    const bookingObject = {
      accountId: accountId,
      bookingSuccessful: true,
      totalNoOfSeatsReserved: finalNoOfSeatsToReserve,
      totalTicketsCost: totalAmountToPay,
    };

    return bookingObject;
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

    //calculate total amount to pay for all tickets
    const totalAmountToPay = this.#calculateTotalAmountToPay(
      allTicketRequestObjects
    );

    //make seatbooking and payment requests
    //assume successful so give user feedback on successful seat reservation.
    // see "README.md "Notes for the examiner from the candidate" 3. for reasoning.
    try {
      const seatReservationAndPaymentRequests = this.#makeBookingRequests(
        accountId,
        finalNoOfSeatsToReserve,
        totalAmountToPay
      );
      return seatReservationAndPaymentRequests;
    } catch (error) {
      throw new Error();
    }
  }
}
