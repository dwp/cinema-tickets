import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService';

import validators from './validators';
import TicketTypeRequest from './lib/TicketTypeRequest.js';

export default class TicketService {
  adultTickets;

  childTickets;

  infantTickets;

  ticketPaymentService;

  seatReservationService;

  constructor() {
    this.adultTickets = 0;
    this.childTickets = 0;
    this.infantTickets = 0;
    this.ticketPaymentService = new TicketPaymentService();
    this.seatReservationService = new SeatReservationService();
  }

  _updateTicketCount(tickets) {
    for (const ticket of tickets) {
      if (ticket.getTicketType() === 'ADULT') {
        this.adultTickets += ticket.getNoOfTickets();
      }

      if (ticket.getTicketType() === 'CHILD') {
        this.childTickets += ticket.getNoOfTickets();
      }

      if (ticket.getTicketType() === 'INFANT') {
        this.infantTickets += ticket.getNoOfTickets();
      }
    }
  }

  _validateRequest(accountId) {
    validators.map((validatorFunction) => {
      const validation = validatorFunction({
        accountId,
        adultTickets: this.adultTickets,
        childTickets: this.childTickets,
        infantTickets: this.infantTickets,
      });

      if (!validation.valid) {
        throw new InvalidPurchaseException(validation.error);
      }
    });

    return true;
  }

  _calculateCost() {
    const costs = {
      adult: 20,
      child: 10,
      infant: 0,
    };

    return this.adultTickets * costs.adult + this.childTickets * costs.child;
  }

  _calculateSeatsRequired() {
    return this.adultTickets + this.childTickets;
  }

  /**
   * Public method to purchase tickets given a valid account ID and at least one TicketTypeRequest
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
    this.ticketPaymentService.makePayment(accountId, this._calculateCost());
    this.seatReservationService.reserveSeat(accountId, this._calculateSeatsRequired())
  }
}
