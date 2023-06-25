import SeatReservationService from '../src/thirdparty/seatbooking/SeatReservationService.js';

export default class MockTicketPaymentService extends SeatReservationService {
  constructor() {
    super();
    this.seatReservations = [];
    this.requestReceived = false;
  }

  reserveSeat(accountId, totalSeats) {
    super.reserveSeat(accountId, totalSeats);
    this.seatReservations.push({ accountId, totalSeats });
    this.requestReceived = true;
  }

  reserveSeatForTicketTypes(accountId, ...ticketTypeRequests) {
    let totalSeats = 0;
    ticketTypeRequests.forEach((ticketTypeRequest) => {
      if (ticketTypeRequest.ticketType !== 'INFANT') {
        totalSeats += ticketTypeRequest.getNoOfTickets();
      }
    });
    this.reserveSeats(accountId, totalSeats);
  }
}
