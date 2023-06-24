import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets(accountId, ...ticketTypeRequests) {
    // throws InvalidPurchaseException
    this._validatePurchace(accountId, ticketTypeRequests);
  }
  _validatePurchace(accountId, ticketTypeRequests) {
    if (accountId <= 0) {
      throw new InvalidPurchaseException('Invalid account ID');
    }

    let totalTickets = 0;

    ticketTypeRequests.forEach((ticketTypeRequest) => {
      totalTickets += ticketTypeRequest.getNoOfTickets();
    });

    if (totalTickets > 20) {
      throw new InvalidPurchaseException(
        'Cannot purchase more than 20 tickets at a time'
      );
    }
  }
}
