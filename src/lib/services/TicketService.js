import TicketTypeRequest from '../TicketTypeRequest.js'
import InvalidPurchaseException from '../InvalidPurchaseException.js'
import TicketPaymentService from '../../thirdparty/paymentgateway/TicketPaymentService.js'
import { accountIDValidator } from '../helpers/validators.js'
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


    ticketPaymentService.makePayment(accountId, 0)
    // throws InvalidPurchaseException
  }
}
