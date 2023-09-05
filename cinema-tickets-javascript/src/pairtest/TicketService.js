import TicketValidator from './lib/TicketValidator.js';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService.js';
import TicketPriceMap from './lib/TicketPriceMap.js';

export default class TicketService {

  #reservationService;
  #ticketPaymentService

  constructor (reservationService, ticketPaymentService) {
    this.#reservationService = reservationService ?? new SeatReservationService();
    this.#ticketPaymentService = ticketPaymentService ?? new TicketPaymentService();
  };

  purchaseTickets(accountId, ...ticketTypeRequests) {
    const ticketCountObject = this.#getTicketCountObject(ticketTypeRequests);
    TicketValidator.validateTicketRequests(accountId, ticketCountObject);

    const totalNumberOfSeats = ticketCountObject.ADULT + ticketCountObject.CHILD;
    this.#reservationService.reserveSeat(accountId, totalNumberOfSeats);

    const totalCostOfOrder = (ticketCountObject.ADULT * TicketPriceMap.ADULT) + (ticketCountObject.CHILD * TicketPriceMap.CHILD) + (ticketCountObject.INFANT * TicketPriceMap.INFANT)
    this.#ticketPaymentService.makePayment(accountId, totalCostOfOrder);
  }

  #getTicketCountObject(ticketTypeRequests) {
    const ticketCounts = {
      'ADULT': 0,
      'CHILD': 0,
      'INFANT': 0
    };

    ticketTypeRequests.forEach((request) => ticketCounts[request.getTicketType()] += request.getNoOfTickets())
    return ticketCounts;
  }

}
