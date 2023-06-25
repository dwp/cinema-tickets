import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService.js';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';
export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */

  constructor(ticketPaymentService, seatReservationService) {
    this._ticketPaymentService = ticketPaymentService;
    this._seatReservationService = seatReservationService;
  }

  purchaseTickets(accountId, ...ticketTypeRequests) {
    // throws InvalidPurchaseException
    this._validatePurchace(accountId, ticketTypeRequests);

    const totalAmountToPay =
      this._calculateTotalAmountToPay(ticketTypeRequests);
    this._ticketPaymentService.requestPayment(accountId, totalAmountToPay);
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

  _calculateTotalAmountToPay(ticketTypeRequests) {
    let totalAmountToPay = 0;

    ticketTypeRequests.forEach((ticketTypeRequest) => {
      switch (ticketTypeRequest.getTicketType()) {
        case 'INFANT':
          totalAmountToPay += 0 * ticketTypeRequest.getNoOfTickets();
          break;
        case 'CHILD':
          totalAmountToPay += 10 * ticketTypeRequest.getNoOfTickets();
          break;
        case 'ADULT':
          totalAmountToPay += 20 * ticketTypeRequest.getNoOfTickets();
          break;
      }
    });
    // console.log(totalAmountToPay);
    return totalAmountToPay;
  }
}
