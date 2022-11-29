/**
 * Immutable Object.
 */

import InvalidPurchaseException from "./InvalidPurchaseException";

export default class TicketTypeRequest {
  /** @type {String} String value of ticket type */
  #type;

  /** @type {Number} Number value representing number of tickets. */
  #noOfTickets;

  constructor(type, noOfTickets) {
    if (!this.#Type.includes(type)) {
      throw new TypeError(
        `type must be ${this.#Type
          .slice(0, -1)
          .join(", ")}, or ${this.#Type.slice(-1)}`
      );
    }

    if (!Number.isInteger(noOfTickets)) {
      throw new TypeError("noOfTickets must be an integer");
    }

    if (noOfTickets < 0) {
      throw new InvalidPurchaseException(
        "ticketNumberError",
        "Numbers of tickets purchased must be 0 or greater."
      );
    }

    this.#type = type;
    this.#noOfTickets = noOfTickets;
  }

  /**
     * Getter which returns number of tickets.
     * 
     * @return {Number} #noOfTickets - the number of tickets.
     */
  getNoOfTickets() {
    return this.#noOfTickets;
  }

  /**
     * Getter which returns type of ticket.
     * 
     * @return {String} #type - thetype of ticket.
     */
  getTicketType() {
    return this.#type;
  }

  /** @type {Array.<String>} Array containing strings representing ticket types */
  #Type = ["ADULT", "CHILD", "INFANT"];
}
