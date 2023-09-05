import TicketValidator from './lib/TicketValidator.js';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';

export default class TicketService {

  #reservationService;

  constructor (reservationService) {
    this.#reservationService = reservationService ?? new SeatReservationService();
  }

  purchaseTickets(accountId, ...ticketTypeRequests) {
    const ticketCountObject = this.#getTicketCountObject(ticketTypeRequests);
    TicketValidator.validateTicketRequests(accountId, ticketCountObject);

    // calculate number of seats
    const totalNumberOfSeats = ticketCountObject.ADULT + ticketCountObject.CHILD;
    this.#reservationService.reserveSeat(accountId, totalNumberOfSeats);
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
