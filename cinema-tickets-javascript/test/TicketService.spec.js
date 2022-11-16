// SUT
import TicketService from '../src/pairtest/TicketService';

import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException';
import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest';

describe('TicketService tests', () => {
  let testTicketService;

  beforeEach(() => {
    testTicketService = new TicketService();
  });

  it('should not allow for more than 20 tickets to be purchased at a time', () => {
    const accountId = 100;
    const adultTicket = new TicketTypeRequest('ADULT', 21);

    expect(() => { testTicketService.purchaseTickets(accountId, adultTicket); }).toThrow(InvalidPurchaseException);
  });

  it('should not allow for zero tickets to be purchased', () => {
    const accountId = 100;
    const adultTicket = new TicketTypeRequest('ADULT', 0);

    expect(() => { testTicketService.purchaseTickets(accountId, adultTicket); }).toThrow(InvalidPurchaseException);
  });

  it('should not allow for a non-integer as an account ID', () => {
    const accountId = 'foo';
    const adultTicket = new TicketTypeRequest('ADULT', 12);

    expect(() => { testTicketService.purchaseTickets(accountId, adultTicket); }).toThrow(InvalidPurchaseException);
  });

  it('should not allow a booking with no adults present', () => {
    const accountId = 300;
    const childTicket = new TicketTypeRequest('CHILD', 2);

    expect(() => { testTicketService.purchaseTickets(accountId, childTicket); }).toThrow(InvalidPurchaseException);
  });

  it('should not allow the number of infant tickets to be greater than the number of adult tickets', () => {
    const accountId = 300;
    const adultTicket = new TicketTypeRequest('ADULT', 3);
    const infantTicket = new TicketTypeRequest('INFANT', 14);

    expect(() => { testTicketService.purchaseTickets(accountId, adultTicket, infantTicket); }).toThrow(InvalidPurchaseException);
  });

  it('should not allow a purchase with an account ID less than 1', () => {
    const accountId = -300;
    const adultTicket = new TicketTypeRequest('ADULT', 1);

    expect(() => { testTicketService.purchaseTickets(accountId, adultTicket); }).toThrow(InvalidPurchaseException);
  });
});
