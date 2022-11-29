/**
 * Immutable Object.
 */

import { MAXIMUM_TICKET_LIMIT, PERMITTED_TICKET_TYPES } from '../constants.js'

export default class TicketTypeRequest {
  #type

  #noOfTickets

  constructor (type, noOfTickets) {
    if (!this.#Type.includes(type)) {
      throw new TypeError(`type must be ${this.#Type.slice(0, -1).join(', ')}, or ${this.#Type.slice(-1)}`)
    }

    if (!Number.isInteger(noOfTickets)) {
      throw new TypeError('noOfTickets must be an integer')
    }

    if (noOfTickets > MAXIMUM_TICKET_LIMIT) {
      throw new Error(`max noOfTickets is ${MAXIMUM_TICKET_LIMIT}!`)
    }

    this.#type = type
    this.#noOfTickets = noOfTickets
  }

  getNoOfTickets () {
    return this.#noOfTickets
  }

  getTicketType () {
    return this.#type
  }

  #Type = PERMITTED_TICKET_TYPES
}
