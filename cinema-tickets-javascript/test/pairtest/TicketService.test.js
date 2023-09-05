import { expect } from 'chai';

import TicketService from '../../src/pairtest/TicketService.js';
import InvalidPurchaseException from '../../src/pairtest/lib/InvalidPurchaseException.js';
import TicketTypeRequest from '../../src/pairtest/lib/TicketTypeRequest.js';

describe('Ticket Service', () => {
  let testService;

  beforeEach(() => {
    testService = new TicketService();
  })

  describe('purchase validation', () => {
    it('should not throw an InvalidPurchaseException if account id is greater than 0', () => {
      const accountId = 1;

      expect(() => testService.purchaseTickets(accountId)).to.not.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if account id is 0', () => {
      const accountId = 0;

      expect(() => testService.purchaseTickets(accountId)).to.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if account id is less than 0', () => {
      const accountId = -1;

      expect(() => testService.purchaseTickets(accountId)).to.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if more than 20 tickets are requested', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('ADULT', 21);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest)).to.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if more than 20 tickets are requested across multiple ticket requests', () => {
      const accountId = 1;
      const ticketTypeRequestAdult = new TicketTypeRequest('ADULT', 10);
      const ticketTypeRequestChild = new TicketTypeRequest('CHILD', 11);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestChild)).to.throw(InvalidPurchaseException);
    });

    it('should not throw an InvalidPurchaseException if 20 tickets are requested across multiple ticket requests', () => {
      const accountId = 1;
      const ticketTypeRequestAdult = new TicketTypeRequest('ADULT', 10);
      const ticketTypeRequestChild = new TicketTypeRequest('CHILD', 10);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestChild)).to.not.throw(InvalidPurchaseException);
    });

    it('should not throw an InvalidPurchaseException if less than 20 tickets are requested', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('ADULT', 10);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest)).to.not.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if a child ticket is purchased without an adult ticket', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('CHILD', 1);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest)).to.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if an infant ticket is purchased without an adult ticket', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('INFANT', 1);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest)).to.throw(InvalidPurchaseException);
    });

    it('should not throw an InvalidPurchaseException if an infant ticket is purchased with an adult ticket', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('INFANT', 1);
      const ticketTypeRequestAdult = new TicketTypeRequest('ADULT', 1);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest, ticketTypeRequestAdult)).to.not.throw(InvalidPurchaseException);
    });

    it('should not throw an InvalidPurchaseException if a child ticket is purchased with an adult ticket', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('CHILD', 1);
      const ticketTypeRequestAdult = new TicketTypeRequest('ADULT', 1);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest, ticketTypeRequestAdult)).to.not.throw(InvalidPurchaseException);
    });

    it('should throw an InvalidPurchaseException if more infant tickets are purchased than adult tickets', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('INFANT', 2);
      const ticketTypeRequestAdult = new TicketTypeRequest('ADULT', 1);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest, ticketTypeRequestAdult)).to.throw(InvalidPurchaseException);
    });

    it('should not throw an InvalidPurchaseException if the same number of infant tickets are purchased as number of adult tickets', () => {
      const accountId = 1;
      const ticketTypeRequest = new TicketTypeRequest('INFANT', 2);
      const ticketTypeRequestAdult = new TicketTypeRequest('ADULT', 2);

      expect(() => testService.purchaseTickets(accountId, ticketTypeRequest, ticketTypeRequestAdult)).to.not.throw(InvalidPurchaseException);
    });
  });
});
