import TicketTypeRequest from '../TicketTypeRequest.js'
import InvalidPurchaseException from '../InvalidPurchaseException.js'
import TicketPaymentService from '../../thirdparty/paymentgateway/TicketPaymentService.js'
import { accountIDValidator, requestValidator } from '../helpers/validators.js'
import { combineTicketRequests } from '../helpers/combineTicketRequests.js'
export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets (accountId, ...ticketTypeRequests) {
    const ticketPaymentService = new TicketPaymentService()

    const accountIDErrors = accountIDValidator(accountId)
    if (accountIDErrors.length > 0) {
      throw new Error(`${accountIDErrors.join(', ')}`)
    }
    const combinedRequest = combineTicketRequests(ticketTypeRequests);

    const requestErrors = requestValidator(combinedRequest);
    ticketPaymentService.makePayment(accountId, 0)
    // throws InvalidPurchaseException
  }
}
