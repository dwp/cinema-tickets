/**
 * Immutable Object.
 */

export default class TicketTypeRequest {
  #type;

  #noOfTickets;

  constructor(type, noOfTickets) {
    if (!this.#Type.includes(type)) {
      throw new TypeError(`type must be ${this.#Type.slice(0, -1).join(', ')}, or ${this.#Type.slice(-1)}`);
    }

    if (!Number.isInteger(noOfTickets)) {
      throw new TypeError('noOfTickets must be an integer');
    }

    /**
     * Additional function to prevent bookings with zero or negative noOfTickets
     * as the above function would let these through currently
     */
     if(Math.sign(noOfTickets) <= 0) {
      throw new TypeError("noOfTickets must be a positive integer")
    }

    this.#type = type;
    this.#noOfTickets = noOfTickets;
  }

  getNoOfTickets() {
    return this.#noOfTickets;
  }

  getTicketType() {
    return this.#type;
  }

  #Type = ['ADULT', 'CHILD', 'INFANT'];
}
