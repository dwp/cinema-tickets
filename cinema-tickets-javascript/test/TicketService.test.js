import { expect } from 'chai';
import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest.js';
import TicketService from '../src/pairtest/TicketService.js';
import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException.js';

describe('TicketService', () => {
  let ticketService;

  beforeEach(() => {
    ticketService = new TicketService();
  });

  it('should throw InvalidPurchaseException when the accountID is not valid', () => {
    const invalidAccountId = -1;
    const adultTicketTypeRequest = new TicketTypeRequest('ADULT', 1);

    expect(() => {
      ticketService.purchaseTickets(invalidAccountId, adultTicketTypeRequest);
    }).to.throw(InvalidPurchaseException, 'Invalid account ID');
  });

  it('should throw InvalidPurchaseException when someone tries to buy more than 20 tickets at a time', () => {
    const accountId = 1;
    const invalidTicketTypeRequest = new TicketTypeRequest('ADULT', 21);

    expect(() => {
      ticketService.purchaseTickets(accountId, invalidTicketTypeRequest);
    }).to.throw(
      InvalidPurchaseException,
      'Cannot purchase more than 20 tickets at a time'
    );
  });

  it('should throw InvalidPurchaseException when someone tries to buy a child or infant ticket without a adult ticket', () => {
    const accountId = 1;
    const infantTicketTypeRequest = new TicketTypeRequest('INFANT', 21);
    const childTicketTypeRequest = new TicketTypeRequest('CHILD', 21);

    expect(() => {
      ticketService.purchaseTickets(accountId, infantTicketTypeRequest);
    }).to.throw(
      InvalidPurchaseException,
      'Cannot purchase a infant ticket without an adult ticket'
    );

    expect(() => {
      ticketService.purchaseTickets(accountId, childTicketTypeRequest);
    }).to.throw(
      InvalidPurchaseException,
      'Cannot purchase a child ticket without an adult ticket'
    );
  });
});
