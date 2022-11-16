/**
 * Immutable Object.
 */
export default class TicketTypeRequest {
  #type;

  #noOfTickets;

  /**
   * @constructor for @see {TicketTypeRequest}
   *
   * @param {String} type - the type of the request, one of:
   *                        - ADULT
   *                        - CHILD
   *                        - INFANT
   * @param {number} noOfTickets - the noOfTickets must be an integer greater than 0.
   */
  constructor(type, noOfTickets) {
    if (!this.#Type.includes(type)) {
      throw new TypeError(`type must be ${this.#Type.slice(0, -1).join(', ')}, or ${this.#Type.slice(-1)}`);
    }

    if (!Number.isInteger(noOfTickets)) {
      throw new TypeError('noOfTickets must be an integer');
    }

    if (noOfTickets < 0) {
      throw new TypeError('noOfTickets must not be a negative number');
    }

    this.#type = type;
    this.#noOfTickets = noOfTickets;
    Object.freeze(this);
  }

  /**
   * @public
   *
   * Getter for @see {#noOfTickets}
   *
   * @returns {number} the number of tickets.
   */
  getNoOfTickets() {
    return this.#noOfTickets;
  }

  /**
   * @public
   *
   * Getter for @see {#type}
   *
   * @returns {String} the type of the tickets.
   */
  getTicketType() {
    return this.#type;
  }

  #Type = ['ADULT', 'CHILD', 'INFANT'];
}
