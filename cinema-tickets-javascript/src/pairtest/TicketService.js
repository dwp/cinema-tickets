import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';

import validators from './validators';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  _validateRequest(accountId, ticketTypeRequests) {
    validators.map((validatorFunction) => {
      const validation = validatorFunction({ accountId, ticketTypeRequests });

      if (validation.error) {
        throw new InvalidPurchaseException(validation.error);
      }
    })

    return true;
  }

  purchaseTickets(accountId, ticketTypeRequests) {
    this._validateRequest(accountId, ticketTypeRequests)
    // throws InvalidPurchaseException
  }
}
