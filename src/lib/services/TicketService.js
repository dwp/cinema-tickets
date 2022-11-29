import TicketTypeRequest from './lib/TicketTypeRequest.js.js'
import InvalidPurchaseException from './lib/InvalidPurchaseException.js.js'

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets (accountId, ...ticketTypeRequests) {
    // throws InvalidPurchaseException
  }
}
