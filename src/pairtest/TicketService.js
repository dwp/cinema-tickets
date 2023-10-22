/* eslint-disable no-console */
/* eslint-disable no-underscore-dangle */
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService.js';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import Validator from './lib/Validator.js';
import Calculator from './lib/Calcuator.js';

export default class TicketService {
  constructor(
    paymentService = new TicketPaymentService(),
    reservationService = new SeatReservationService(),
  ) {
    this.paymentService = paymentService;
    this.seatReservationService = reservationService;
  }

  purchaseTickets(accountId, ...ticketTypeRequests) {
    if (!accountId || !ticketTypeRequests.length) {
      throw new Error('Invalid accountId or ticket requests');
    }

    try {
      if (!TicketService._isValidRequest(...ticketTypeRequests)) {
        throw new InvalidPurchaseException();
      }

      const totalCost = Calculator.getTotalCost(...ticketTypeRequests);
      const totalSeats = Calculator.getTotalSeats(...ticketTypeRequests);

      this._makePayment(accountId, totalCost);
      this._reserveSeats(accountId, totalSeats);
    } catch (error) {
      console.error('Error while purchasing tickets');
      throw error;
    }
  }

  static _isValidRequest(...ticketTypeRequests) {
    return Validator.isTicketCountValid(...ticketTypeRequests)
      && Validator.hasAccompanyingAdult(...ticketTypeRequests);
  }

  _makePayment(accountId, amount) {
    try {
      this.paymentService.makePayment(accountId, amount);
    } catch (error) {
      console.error('Error while making payment');
      throw new Error('Failed to make payment.');
    }
  }

  _reserveSeats(accountId, totalSeats) {
    try {
      this.seatReservationService.reserveSeat(accountId, totalSeats);
    } catch (error) {
      console.error('Error while reserving seats');
      throw new Error('Failed to reserve seats.');
    }
  }
}
