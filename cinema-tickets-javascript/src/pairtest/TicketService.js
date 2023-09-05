import TicketValidator from './lib/TicketValidator.js';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets(accountId, ...ticketTypeRequests) {
    const ticketCountObject = this.#getTicketCountObject(ticketTypeRequests);
    TicketValidator.validateTicketRequests(accountId, ticketCountObject);
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

}
