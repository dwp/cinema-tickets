/* eslint-disable no-unused-expressions */
import chai from 'chai';
import Validator from '../src/pairtest/lib/Validator.js';

const { expect } = chai;

describe('Validator', () => {
  describe('isTicketCountValid', () => {
    it('should return true for valid ticket count', () => {
      const ticketRequests = [
        { type: 'ADULT', count: 10 },
        { type: 'CHILD', count: 5 },
      ];

      const result = Validator.isTicketCountValid(...ticketRequests);

      expect(result).to.be.true;
    });

    it('should return false for ticket count over 20', () => {
      const ticketRequests = [
        { type: 'ADULT', count: 15 },
        { type: 'CHILD', count: 7 },
      ];

      const result = Validator.isTicketCountValid(...ticketRequests);

      expect(result).to.be.false;
    });
  });

  describe('hasAccompanyingAdult', () => {
    it('should return true when there are accompanying adults', () => {
      const ticketRequests = [
        { type: 'ADULT', count: 2 },
        { type: 'CHILD', count: 5 },
      ];

      const result = Validator.hasAccompanyingAdult(...ticketRequests);

      expect(result).to.be.true;
    });

    it('should return false when there are no accompanying adults', () => {
      const ticketRequests = [
        { type: 'CHILD', count: 5 },
      ];

      const result = Validator.hasAccompanyingAdult(...ticketRequests);

      expect(result).to.be.false;
    });
  });
});
