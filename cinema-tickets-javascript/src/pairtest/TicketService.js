import TicketValidator from './lib/TicketValidator.js';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService.js';
import TicketPriceMap from './lib/TicketPriceMap.js';
import { INITIAL_TICKET_COUNT_OBJECT } from './lib/Constants.js';

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

    this.#reserveSeats(accountId, ticketCountObject);
    this.#makePayment(accountId, ticketCountObject)
  }

  #makePayment(accountId, ticketCountObject) {
    const totalCostOfOrder = (ticketCountObject.ADULT * TicketPriceMap.ADULT) + (ticketCountObject.CHILD * TicketPriceMap.CHILD) + (ticketCountObject.INFANT * TicketPriceMap.INFANT);
    this.#ticketPaymentService.makePayment(accountId, totalCostOfOrder);
  }

  #reserveSeats(accountId, ticketCountObject) {
    const totalNumberOfSeats = ticketCountObject.ADULT + ticketCountObject.CHILD;
    this.#reservationService.reserveSeat(accountId, totalNumberOfSeats);
  }

  #getTicketCountObject(ticketTypeRequests) {
    const ticketCounts = structuredClone(INITIAL_TICKET_COUNT_OBJECT);
    ticketTypeRequests.forEach((request) => ticketCounts[request.getTicketType()] += request.getNoOfTickets())
    return ticketCounts;
  }
}
