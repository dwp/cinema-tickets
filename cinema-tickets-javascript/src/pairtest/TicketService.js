import InvalidPurchaseException from './lib/InvalidPurchaseException.js';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets(accountId, ...ticketTypeRequests) {
    const ticketCountObject = this.#getTicketCountObject(ticketTypeRequests);
    this.#validateTicketRequests(accountId, ticketCountObject);
  }

  #getTicketCountObject(ticketTypeRequests) {
    const ticketCounts = {
      'ADULT': 0,
      'CHILD': 0,
      'INFANT': 0
    };

    ticketTypeRequests.forEach((request) => ticketCounts[request.getTicketType()] += request.getNoOfTickets())
    return ticketCounts;
  }

  #validateTicketRequests(accountId, ticketCounts) {
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

  #calculateTotalNumberOfTickets(ticketCounts) {
    return Object.values(ticketCounts).reduce((total, value) => total + value, 0);
  }

  #isChildOrInfantTicketsWithoutAdult(ticketCounts) {
    return (ticketCounts.CHILD >= 1 || ticketCounts.INFANT >= 1) && ticketCounts.ADULT === 0;
  }

  #isMoreInfantsThanAdults(ticketCounts) {
    return ticketCounts.INFANT > ticketCounts.ADULT;
  }

}
