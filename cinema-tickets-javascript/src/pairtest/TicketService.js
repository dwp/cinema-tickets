import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import { init } from 'mocha/lib/cli/commands.js';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets(accountId, ...ticketTypeRequests) {
    if (accountId <= 0) {
      throw new InvalidPurchaseException();
    }

    const ticketCounts = {
      'ADULT': 0,
      'CHILD': 0,
      'INFANT': 0
    };

    ticketTypeRequests.forEach((request) => ticketCounts[request.getTicketType()] += request.getNoOfTickets())

    if (Object.values(ticketCounts).reduce((total, value) => total + value, 0) > 20) {
      throw new InvalidPurchaseException();
    }

    if ((ticketCounts.CHILD >= 1 || ticketCounts.INFANT >= 1) && ticketCounts.ADULT === 0) {
      throw new InvalidPurchaseException();
    }

    if (ticketCounts.INFANT > ticketCounts.ADULT) {
      throw new InvalidPurchaseException();
    }
  }
}
