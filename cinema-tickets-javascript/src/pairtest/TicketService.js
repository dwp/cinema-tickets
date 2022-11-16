import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService';

import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import TicketTypeRequest from './lib/TicketTypeRequest.js';

import validators from './validators';
import { TICKET_PRICES } from './constants';

export default class TicketService {
  #ticketPaymentService;

  #seatReservationService;

  /**
   * @constructor for @see {TicketService}
   */
  constructor() {
    this.#ticketPaymentService = new TicketPaymentService();
    this.#seatReservationService = new SeatReservationService();
  }

  /**
   * @private
   *
   * Method to collect and group together the count for all adult, child and infant tickets for
   * a single payment request.
   *
   * @param {Array} tickets - an array of @see {TicketTypeRequest}
   *
   * @return {Object} an object with the count of all ticket types.
   */
  _groupTickets(tickets) {
    const allTickets = {
      adultTickets: 0,
      childTickets: 0,
      infantTickets: 0,
    };

    for (const ticket of tickets) {
      if (ticket.getTicketType() === 'ADULT') {
        allTickets.adultTickets += ticket.getNoOfTickets();
      }

      if (ticket.getTicketType() === 'CHILD') {
        allTickets.childTickets += ticket.getNoOfTickets();
      }

      if (ticket.getTicketType() === 'INFANT') {
        allTickets.infantTickets += ticket.getNoOfTickets();
      }
    }

    return allTickets;
  }

  /**
   * @private
   *
   * Method to validate the accountId, alongside the ticket requests when purchasing tickets.
   *
   * @param {number} accountId - the account ID must be an integer greater than 0.
   * @param {Object} tickets - an object containing the number of each ticket type.
   */
  _validateRequest(accountId, tickets) {
    const { adultTickets, childTickets, infantTickets } = tickets;
    validators.map((validatorFunction) => {
      const validation = validatorFunction({
        accountId,
        adultTickets,
        childTickets,
        infantTickets,
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
  _calculateCost(adultTickets, childTickets, infantTickets) {
    return adultTickets * TICKET_PRICES.adult + childTickets * TICKET_PRICES.child + infantTickets * TICKET_PRICES.infant;
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
  _calculateSeatsRequired(adultTicket, childTickets) {
    return adultTickets + childTickets;
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
    const groupedTickets = this._groupTickets(ticketTypeRequests);

    this._validateRequest(accountId, groupedTickets);
    this.#ticketPaymentService.makePayment(accountId, this._calculateCost());
    this.#seatReservationService.reserveSeat(accountId, this._calculateSeatsRequired());
  }
}
