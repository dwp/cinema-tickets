import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService.js';

import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';

const TicketType = {
  ADULT: 'ADULT',
  CHILD: 'CHILD',
  INFANT: 'INFANT',
};

const TicketTypePrice = {
  [TicketType.ADULT]: 20,
  [TicketType.CHILD]: 10,
  [TicketType.INFANT]: 0,
};

export default class TicketService {
  constructor() {
    this.reserveSeat = new SeatReservationService();
    this.payTicket = new TicketPaymentService();
  }

  #validateAccountId(accountId) {
    if (!Number.isInteger(accountId)) {
      throw new TypeError('accountId must be an integer');
    }
    
    if (accountId < 1) {
      throw new InvalidPurchaseException('invalid accountId');
    }
  }

  #validateTicketRequest(ticketTypeRequests) {
    const totalChildAndInfantTickets = ticketTypeRequests.reduce((accumulator, ticketTypeRequest) => {
      if (ticketTypeRequest.getTicketType() === TicketType.CHILD || ticketTypeRequest.getTicketType() === TicketType.INFANT) {
        return accumulator + ticketTypeRequest.getNoOfTickets();
      }
      return accumulator;
    }, 0);

    const totalAdultTickets = ticketTypeRequests.reduce((accumulator, ticketTypeRequest) => {
      if (ticketTypeRequest.getTicketType() === TicketType.ADULT) {
        return accumulator + ticketTypeRequest.getNoOfTickets();
      }
      return accumulator;
    }, 0);

    if (totalChildAndInfantTickets > totalAdultTickets) {
      throw new InvalidPurchaseException('total number of child and infant tickets must be less than or equal to the number of adult tickets');
    }

    if (totalChildAndInfantTickets + totalAdultTickets < 1) {
      throw new InvalidPurchaseException('no of tickets must be greater than or equal to 1');
    }

    if (totalChildAndInfantTickets + totalAdultTickets > 20) {
      throw new InvalidPurchaseException('no of tickets must be less than or equal to 20');
    }
  }

  #createTickets(ticketTypeRequests) {
    return ticketTypeRequests.map(ticketTypeRequest => {
      return new TicketTypeRequest(ticketTypeRequest.type, ticketTypeRequest.noOfTickets)
    });
  }

  #calculateTotalPrice(tickets) {
    return tickets.reduce(
      (accumulator, tickets) => accumulator + (tickets.getNoOfTickets() * TicketTypePrice[tickets.getTicketType()]),
      0
    );
  }

  #calculateNoOfSeats(tickets) {
    return tickets.reduce((accumulator, currentValue) => {
      if (currentValue.getTicketType() !== TicketType.INFANT) {
        return accumulator + currentValue.getNoOfTickets();
      }
      return accumulator;
    }, 0);
  }

  /**
   * Should only have private methods other than the one below.
   */
  purchaseTickets(accountId, ...ticketTypeRequests) {
    this.#validateAccountId(accountId);

    const tickets = this.#createTickets(ticketTypeRequests);

    this.#validateTicketRequest(tickets);

    const noOfSeats = this.#calculateNoOfSeats(tickets);
    const totalPrice = this.#calculateTotalPrice(tickets);

    this.reserveSeat.reserveSeat(accountId, noOfSeats);
    this.payTicket.makePayment(accountId, totalPrice);
  }
}
