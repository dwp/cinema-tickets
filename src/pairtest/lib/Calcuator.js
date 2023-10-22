/* eslint-disable no-restricted-globals */
import TICKET_PRICES from './TicketTypes.js';

class Calculator {
  static getTotalCost(...ticketTypeRequests) {
    const result = ticketTypeRequests.reduce((
      total,
      request,
    ) => total + request.count * TICKET_PRICES[request.type], 0);
    return isNaN(result) ? 0 : result;
  }

  static getTotalSeats(...ticketTypeRequests) {
    return ticketTypeRequests.reduce((total, request) => (request.type === 'INFANT' ? total : total + request.count), 0);
  }
}

export default Calculator;
