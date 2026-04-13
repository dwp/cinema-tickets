import TicketTypeRequest from './lib/TicketTypeRequest.js';
import InvalidPurchaseException from './lib/InvalidPurchaseException.js';
import TicketPaymentService from '../thirdparty/paymentgateway/TicketPaymentService.js';
import SeatReservationService from '../thirdparty/seatbooking/SeatReservationService.js';

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */
  #validateRequest(accountid, ticketTypeRequests) {
    if(!Number.isInteger(accountid) || accountid <= 0) {
        throw new InvalidPurchaseException("Account ID must be greater than 0");
    }

    if(ticketTypeRequests.length === 0) {
        throw new InvalidPurchaseException("At least one ticket type request must be provided");
    }

    const validRequests = ticketTypeRequests.every(request => request instanceof TicketTypeRequest);
    
    if(!validRequests) {
        throw new InvalidPurchaseException("All requests must be instances of TicketTypeRequest");
    }

    const totalTickets = ticketTypeRequests.reduce((total, r) => total + r.getNoOfTickets(), 0);
    if(totalTickets > 25) {
        throw new InvalidPurchaseException("Cannot purchase more than 25 tickets");
    }
    const hasAdultTicket = ticketTypeRequests.some(request => request.getTicketType() === "ADULT");
    if(!hasAdultTicket) {
        throw new InvalidPurchaseException("At least one adult ticket must be purchased");
    }
  }

  #calculateTotalPrice(ticketTypeRequests) {
    return ticketTypeRequests.reduce((total, request) => {
        switch (request.getTicketType()) {
            case "ADULT":
                return total + (request.getNoOfTickets() * 25);
            case "CHILD":
                return total + (request.getNoOfTickets() * 15);
            case "INFANT":
                return total;
            default:
                throw new InvalidPurchaseException("Invalid ticket type");
        }
    }, 0);
  }

  #calculateSeatsToReserve(ticketTypeRequests) {
    return ticketTypeRequests.reduce((total, request) => {
        if (request.getTicketType() === "ADULT" || request.getTicketType() === "CHILD") {
            return total + request.getNoOfTickets();
        } 
        return total;
    }
    , 0);
  }


  purchaseTickets(accountId, ...ticketTypeRequests) {
    
      this.#validateRequest(accountId, ticketTypeRequests);
      const totalPrice = this.#calculateTotalPrice(ticketTypeRequests);
      const totalSeatsToReserve = this.#calculateSeatsToReserve(ticketTypeRequests);

      new TicketPaymentService().makePayment(accountId, totalPrice);
      new SeatReservationService().reserveSeat(accountId, totalSeatsToReserve);
  }
}
