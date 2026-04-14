import TicketPaymentService from "../thirdparty/paymentgateway/TicketPaymentService.js";
import SeatReservationService from "../thirdparty/seatbooking/SeatReservationService.js";
import InvalidPurchaseException from "./lib/InvalidPurchaseException.js";

const TICKET_PRICE = {
  ADULT: 25,
  CHILD: 15,
  INFANT: 0,
};

const MAX_TICKETS = 25;

export default class TicketService {
  /**
   * Should only have private methods other than the one below.
   */
  constructor(
    ticketPaymentService = new TicketPaymentService(),
    seatReservationService = new SeatReservationService(),
  ) {
    this.ticketPaymentService = ticketPaymentService;
    this.seatReservationService = seatReservationService;
  }

  purchaseTickets(accountId, ...ticketTypeRequests) {
    if (!Number.isInteger(accountId) || accountId <= 0) {
      throw new InvalidPurchaseException("Invalid account id");
    }

    this.#validateRequests(ticketTypeRequests);

    const { adultCount, childCount, infantCount } =
      this.#summariseRequests(ticketTypeRequests);

    this.#validateBusinessRules(adultCount, childCount, infantCount);

    const totalAmount = this.#calculateTotalAmount(adultCount, childCount);
    const totalSeats = this.#calculateTotalSeats(adultCount, childCount);

    this.ticketPaymentService.makePayment(accountId, totalAmount);
    this.seatReservationService.reserveSeat(accountId, totalSeats);
  }

  #validateRequests(ticketTypeRequests) {
    if (!ticketTypeRequests || ticketTypeRequests.length === 0) {
      throw new InvalidPurchaseException("No ticket requests provided");
    }
    ticketTypeRequests.forEach((ticketTypeRequest) => {
      if (ticketTypeRequest.getNoOfTickets() <= 0) {
        throw new InvalidPurchaseException("Invalid ticket quantity");
      }
    });
  }

  #summariseRequests(ticketTypeRequests) {
    let adultCount = 0;
    let childCount = 0;
    let infantCount = 0;

    ticketTypeRequests.forEach((request) => {
      const type = request.getTicketType();
      const quantity = request.getNoOfTickets();

      if (type === "ADULT") adultCount += quantity;
      if (type === "CHILD") childCount += quantity;
      if (type === "INFANT") infantCount += quantity;
    });

    return { adultCount, childCount, infantCount };
  }

  #validateBusinessRules(adultCount, childCount, infantCount) {
    if (adultCount === 0 && (childCount > 0 || infantCount > 0)) {
      throw new InvalidPurchaseException(
        "Child and infant tickets require an adult",
      );
    }

    const totalTickets = adultCount + childCount + infantCount;

    if (totalTickets > MAX_TICKETS) {
      throw new InvalidPurchaseException(
        "Cannot purchase more than 25 tickets",
      );
    }

    if (infantCount > adultCount) {
      throw new InvalidPurchaseException(
        "Infant tickets cannot exceed adult tickets",
      );
    }
  }

  #calculateTotalAmount(adultCount, childCount) {
    return adultCount * TICKET_PRICE.ADULT + childCount * TICKET_PRICE.CHILD;
  }

  #calculateTotalSeats(adultCount, childCount) {
    return adultCount + childCount;
  }
}
