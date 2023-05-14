/**
 * Immutable Object.
 */

import { MAXIMUM_TICKET_LIMIT, PERMITTED_TICKET_TYPES } from '../constants.js'
import { HttpError } from './errors/HttpError.js'

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
      throw new HttpError(`max noOfTickets is ${MAXIMUM_TICKET_LIMIT}!`, 400)
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
