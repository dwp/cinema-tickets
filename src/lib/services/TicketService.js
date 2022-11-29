import TicketPaymentService from '../../thirdparty/paymentgateway/TicketPaymentService.js'
import { accountIDValidator, requestValidator } from '../helpers/validators.js'
import { combineTicketRequests } from '../helpers/combineTicketRequests.js'
import calculatePayment from '../helpers/calculatePayment.js'
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

    const combinedRequest = combineTicketRequests(ticketTypeRequests)
    const requestErrors = requestValidator(combinedRequest);

    if(requestErrors.length > 0) {
      throw new Error(`${requestErrors.join(', ')}`)
    }

    const totalPaymentAmount = calculatePayment(combinedRequest)
    ticketPaymentService.makePayment(accountId, totalPaymentAmount)
  }
}
