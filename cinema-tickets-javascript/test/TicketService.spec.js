// SUT
import TicketService from '../src/pairtest/TicketService';
import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException';

/**
 * Business rules
 * 
 * - 3 ticket types
 *      - Infant: £0, Child: £10, Adult: £20
 * - Max 20 tickets can be purchased in one transaction
 * - Infants must sit on an adults lap, are not allocated a seat
 * - Child and infant tickets cannot be purchased without an adult
 * - Accounts with an ID greater than 0 are valid
 * - 
 */

describe('TicketService tests', () => {
  let testTicketService;

  beforeEach(() => {
    testTicketService = new TicketService();
  });

  it('should not accept any other ticket types other than Adult, Child, Infant', () => {
    const accountId = 300;
    const ticketTypeRequests = {
      Adult: 1,
      Foo: 8,
    };

    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow for more than 20 tickets to be purchased at a time', () => {
    const accountId = 100;
    const ticketTypeRequests = {
      Adult: 21,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow for zero tickets to be purchased', () => {
    const accountId = 100;
    const ticketTypeRequests = {
      Adult: 0,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow for a non-integer as an account ID', () => {
    const accountId = 'foo';
    const ticketTypeRequests = {
      Adult: 12,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow for a non-integer for the number of specific ticket types', () => {
    const accountId = 100;
    const ticketTypeRequests = {
      Adult: 10,
      Child: 'bar',
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow a booking with no adults present', () => {
    const accountId = 300;
    const ticketTypeRequests = {
      Child: 2,
      Intant: 1,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow the number of infant tickets to be greater than the number of adult tickets', () => {
    const accountId = 300;
    const ticketTypeRequests = {
      Adult: 2,
      Infant: 4,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow a purchase with an account ID less than 1', () => {
    const accountId = -300;
    const ticketTypeRequests = {
      Adult: 1,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });

  it('should not allow for any negative ticket numbers to be purchased', () => {
    const accountId = 300;
    const ticketTypeRequests = {
      Adult: -1,
      Child: 3,
    };
    
    expect(testTicketService.purchaseTickets(accountId, ticketTypeRequests)).toThrow(InvalidPurchaseException);
  });
});
