import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService';

import validators from './validators';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */
  adultTickets;

  childTickets;

  infantTickets;

  ticketPaymentService;

  constructor() {
    this.adultTickets = 0;
    this.childTickets = 0;
    this.infantTickets = 0;
    this.ticketPaymentService = new TicketPaymentService();
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

  purchaseTickets(accountId, ...ticketTypeRequests) {
    this._updateTicketCount(ticketTypeRequests);
    this._validateRequest(accountId);
    this.ticketPaymentService.makePayment(this._calculateCost());
    // throws InvalidPurchaseException
  }
}
