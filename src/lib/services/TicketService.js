import TicketTypeRequest from '../TicketTypeRequest.js'
import InvalidPurchaseException from '../InvalidPurchaseException.js'
import TicketPaymentService from '../../thirdparty/paymentgateway/TicketPaymentService.js'
export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets (accountId, ...ticketTypeRequests) {
    const ticketPaymentService = new TicketPaymentService();
    
    ticketPaymentService.makePayment(accountId, 0)
    // throws InvalidPurchaseException
  }
}
