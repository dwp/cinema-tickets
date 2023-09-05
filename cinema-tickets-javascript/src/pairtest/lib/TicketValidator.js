import InvalidPurchaseException from './InvalidPurchaseException.js';

export default class TicketValidator {
  static validateTicketRequests(accountId, ticketCounts) {
    if (accountId <= 0) {
      throw new InvalidPurchaseException();
    }

    if (this.#calculateTotalNumberOfTickets(ticketCounts) > 20) {
      throw new InvalidPurchaseException();
    }

    if (this.#isChildOrInfantTicketsWithoutAdult(ticketCounts)) {
      throw new InvalidPurchaseException();
    }

    if (this.#isMoreInfantsThanAdults(ticketCounts)) {
      throw new InvalidPurchaseException();
    }
  }

  static #calculateTotalNumberOfTickets(ticketCounts) {
    return Object.values(ticketCounts).reduce((total, value) => total + value, 0);
  }

  static #isChildOrInfantTicketsWithoutAdult(ticketCounts) {
    return (ticketCounts.CHILD >= 1 || ticketCounts.INFANT >= 1) && ticketCounts.ADULT === 0;
  }

  static #isMoreInfantsThanAdults(ticketCounts) {
    return ticketCounts.INFANT > ticketCounts.ADULT;
  }
}
