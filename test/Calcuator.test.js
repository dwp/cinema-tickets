import chai from 'chai';
import Calculator from '../src/pairtest/lib/Calcuator.js';
import TICKET_PRICES from '../src/pairtest/lib/TicketTypes.js';

const { expect } = chai;

describe('Calculator', () => {
  describe('getTotalCost', () => {
    it('should return 0 when no arguments are passed', () => {
      const cost = Calculator.getTotalCost();
      expect(cost).to.equal(0);
    });

    it('should calculate the correct cost for valid ticket types', () => {
      const ticketTypeRequests = [
        { type: 'ADULT', count: 2 },
        { type: 'CHILD', count: 3 },
      ];
      const expectedCost = (2 * TICKET_PRICES.ADULT) + (3 * TICKET_PRICES.CHILD);

      const cost = Calculator.getTotalCost(...ticketTypeRequests);
      expect(cost).to.equal(expectedCost);
    });

    it('should return 0 for an invalid ticket type', () => {
      const ticketTypeRequests = [
        { type: 'INVALID', count: 3 },
      ];
      const cost = Calculator.getTotalCost(...ticketTypeRequests);
      expect(cost).to.equal(0);
    });
  });

  describe('getTotalSeats', () => {
    it('should return 0 when no arguments are passed', () => {
      const seats = Calculator.getTotalSeats();
      expect(seats).to.equal(0);
    });

    it('should calculate the correct seat count excluding INFANT tickets', () => {
      const ticketTypeRequests = [
        { type: 'ADULT', count: 2 },
        { type: 'INFANT', count: 3 },
      ];
      const expectedSeats = 2;

      const seats = Calculator.getTotalSeats(...ticketTypeRequests);
      expect(seats).to.equal(expectedSeats);
    });

    it('should return 0 when only INFANT tickets are provided', () => {
      const ticketTypeRequests = [
        { type: 'INFANT', count: 5 },
      ];
      const seats = Calculator.getTotalSeats(...ticketTypeRequests);
      expect(seats).to.equal(0);
    });
  });
});
