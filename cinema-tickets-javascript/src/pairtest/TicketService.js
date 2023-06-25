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
    let infantTickets = 0;
    let childTickets = 0;
    let adultTickets = 0;

    ticketTypeRequests.forEach((ticketTypeRequest) => {
      totalTickets += ticketTypeRequest.getNoOfTickets();

      switch (ticketTypeRequest.getTicketType()) {
        case 'INFANT':
          infantTickets += ticketTypeRequest.getNoOfTickets();
          break;
        case 'CHILD':
          childTickets += ticketTypeRequest.getNoOfTickets();
          break;
        case 'ADULT':
          adultTickets += ticketTypeRequest.getNoOfTickets();
          break;
      }
    });

    if (totalTickets > 20) {
      throw new InvalidPurchaseException(
        'Cannot purchase more than 20 tickets at a time'
      );
    }

    if (adultTickets === 0 && infantTickets > 0) {
      throw new InvalidPurchaseException(
        'Cannot purchase a infant ticket without an adult ticket'
      );
    }

    if (adultTickets === 0 && childTickets > 0) {
      throw new InvalidPurchaseException(
        'Cannot purchase a child ticket without an adult ticket'
      );
    }
  }
}
