import TicketPaymentService from '../../thirdparty/paymentgateway/TicketPaymentService.js'
import SeatReservationService from '../../thirdparty/seatbooking/SeatReservationService.js'

import { accountIDValidator, requestValidator } from '../helpers/validators.js'
import { combineTicketRequests } from '../helpers/combineTicketRequests.js'
import calculatePayment from '../helpers/calculatePayment.js'
import calculateSeatReservation from '../helpers/calculateSeatReservation.js'
import { HttpError } from '../errors/HttpError.js'
export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  purchaseTickets (accountId, ...ticketTypeRequests) {
    const accountIDError = accountIDValidator(accountId)
    if (accountIDError) {
      throw new HttpError(accountIDError, 400)
    }

    const combinedRequest = combineTicketRequests(ticketTypeRequests)
    const requestErrors = requestValidator(combinedRequest)

    if (requestErrors.length > 0) {
      throw new HttpError(`${requestErrors.join(', ')}`, 400)
    }

    // spin up connectors now parameters are validated
    const ticketPaymentService = new TicketPaymentService()
    const seatReservationService = new SeatReservationService()

    const totalPaymentAmount = calculatePayment(combinedRequest)
    ticketPaymentService.makePayment(accountId, totalPaymentAmount)

    const totalNumberOfSeats = calculateSeatReservation(combinedRequest)
    seatReservationService.reserveSeat(accountId, totalNumberOfSeats)

    const plural = totalNumberOfSeats > 1 ? 'seats' : 'seat'

    return {
      code: 200,
      message: `You have successfully reserved ${totalNumberOfSeats} ${plural} for £${totalPaymentAmount}.`
    }
  }
}
