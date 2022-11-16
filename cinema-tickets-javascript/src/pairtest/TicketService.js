import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService';

import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import TicketTypeRequest from './lib/TicketTypeRequest.js';

import validators from './validators';
import { TICKET_PRICES } from './constants';

export default class TicketService {
  #adultTickets;

  #childTickets;

  #infantTickets;

  #ticketPaymentService;

  #seatReservationService;

  /**
   * @constructor for @see {TicketService}
   */
  constructor() {
    this.#adultTickets = 0;
    this.#childTickets = 0;
    this.#infantTickets = 0;
    this.#ticketPaymentService = new TicketPaymentService();
    this.#seatReservationService = new SeatReservationService();
  }

  /**
   * @private
   *
   * Method to update the ticket class parameters for @see {TicketService} when a new payment
   * request comes in.
   *
   * @param {Array} tickets - an array of @see {TicketTypeRequest}
   */
  _updateTicketCount(tickets) {
    for (const ticket of tickets) {
      if (ticket.getTicketType() === 'ADULT') {
        this.#adultTickets += ticket.getNoOfTickets();
      }

      if (ticket.getTicketType() === 'CHILD') {
        this.#childTickets += ticket.getNoOfTickets();
      }

      if (ticket.getTicketType() === 'INFANT') {
        this.#infantTickets += ticket.getNoOfTickets();
      }
    }
  }

  /**
   * @private
   *
   * Method to validate the accountId, alongside the ticket requests when purchasing tickets.
   *
   * @param {number} accountId - the account ID must be an integer greater than 0.
   */
  _validateRequest(accountId) {
    validators.map((validatorFunction) => {
      const validation = validatorFunction({
        accountId,
        adultTickets: this.#adultTickets,
        childTickets: this.#childTickets,
        infantTickets: this.#infantTickets,
      });

      if (!validation.valid) {
        throw new InvalidPurchaseException(validation.error);
      }
    });
  }

  /**
   * @private
   *
   * Method to calculate the cost of the tickets based on the prices defined in
   * @see {TICKET_PRICES} - these values are subject to change.
   *
   * @returns {number} the total cost of the reserved seats in £.
   */
  _calculateCost() {
    return this.#adultTickets * TICKET_PRICES.adult + this.#childTickets * TICKET_PRICES.child + this.#infantTickets * TICKET_PRICES.infant;
  }

  /**
   * @private
   *
   * Method to calculate the number of seats required.
   * Only adult tickets and child tickets are factored in because infants are expected to sit on the
   * lap of an adult.
   *
   * @returns {number} the total number of seats required.
   */
  _calculateSeatsRequired() {
    return this.#adultTickets + this.#childTickets;
  }

  /**
   * @public
   *
   * Method to purchase tickets given a valid account ID and at least one TicketTypeRequest
   * objects.
   * This method will validate the accountId, and ticketTypeRequests, and if valid will call off
   * to the TicketPaymentService, then to the SeatReservation service respectively to complete
   * the ticket purchasing operation.
   *
   * @param {number} accountId - the account ID must be an integer greater than 0.
   * @param {TicketTypeRequest} ticketTypeRequests - a number of valid instances of
   *                                                 TicketTypeRequest classes.
   *
   * @throws {InvalidPurchaseException} - when either the provided accountId or ticketTypeRequests
   *                                      fail validation.
   */
  purchaseTickets(accountId, ...ticketTypeRequests) {
    this._updateTicketCount(ticketTypeRequests);
    this._validateRequest(accountId);
    this.#ticketPaymentService.makePayment(accountId, this._calculateCost());
    this.#seatReservationService.reserveSeat(accountId, this._calculateSeatsRequired());
  }
}
