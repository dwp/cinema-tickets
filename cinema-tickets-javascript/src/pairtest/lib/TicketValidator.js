import InvalidPurchaseException from './InvalidPurchaseException.js';
import { MAX_NUMBER_TICKETS } from './Constants.js';
import {
  INVALID_ACCOUNT_ID,
  MORE_INFANTS_THAN_ADULTS,
  NO_ADULT_TICKET,
  TOO_MANY_TICKETS
} from './Errors.js';

export default class TicketValidator {
  static validateTicketRequests(accountId, ticketCounts) {
    if (accountId <= 0) {
      throw new InvalidPurchaseException(INVALID_ACCOUNT_ID);
    }

    if (this.#calculateTotalNumberOfTickets(ticketCounts) > MAX_NUMBER_TICKETS) {
      throw new InvalidPurchaseException(TOO_MANY_TICKETS);
    }

    if (this.#isChildOrInfantTicketsWithoutAdult(ticketCounts)) {
      throw new InvalidPurchaseException(NO_ADULT_TICKET);
    }

    if (this.#isMoreInfantsThanAdults(ticketCounts)) {
      throw new InvalidPurchaseException(MORE_INFANTS_THAN_ADULTS);
    }
  }

  static #calculateTotalNumberOfTickets(ticketCounts) {
    return Object.values(ticketCounts).reduce((total, value) => total + value, 0);
  }

  static #isChildOrInfantTicketsWithoutAdult(ticketCounts) {
    return (ticketCounts.CHILD >= 1 || ticketCounts.INFANT >= 1) && ticketCounts.ADULT === 0;
  }

  static #isMoreInfantsThanAdults(ticketCounts) {
    return ticketCounts.INFANT > ticketCounts.ADULT;
  }
}
