import { expect } from 'chai';
import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest.js';
import TicketService from '../src/pairtest/TicketService.js';
import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException.js';
import MockTicketPaymentService from '../mocks/MockTicketPaymentService.js';
import MockSeatReservationService from '../mocks/MockSeatReservationService.js';

describe('TicketService', () => {
  let ticketService;

  beforeEach(() => {
    const mockTicketPaymentService = new MockTicketPaymentService();
    const mockSeatReservationService = new MockSeatReservationService();
    ticketService = new TicketService(
      mockTicketPaymentService,
      mockSeatReservationService
    );
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
    const infantTicketTypeRequest = new TicketTypeRequest('INFANT', 1);
    const childTicketTypeRequest = new TicketTypeRequest('CHILD', 1);

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

  it('should calculate the correct total for the tickets bought and make a request to the provided ticket payment service', () => {
    const accountId = 1;
    const dadTypeRequests1 = new TicketTypeRequest('ADULT', 1);
    const momTypeRequests = new TicketTypeRequest('ADULT', 1);
    const childTypeRequests = new TicketTypeRequest('CHILD', 1);
    const infantTypeRequests = new TicketTypeRequest('INFANT', 1);

    ticketService.purchaseTickets(
      accountId,
      dadTypeRequests1,
      momTypeRequests,
      childTypeRequests,
      infantTypeRequests
    );

    // £20 + £20 + £10 + £0 = £50 total
    // 2 adults, 1 child, 1 infant
    // 2 adults = 2 * £20 = £40
    // 1 child = 1 * £10 = £10
    // 1 infant = 1 * £0 = £0
    // £40 + £10 + £0 = £50 total
    const expectedPaymentRequest =
      ticketService._ticketPaymentService.paymentRequests[0];

    expect(expectedPaymentRequest).to.deep.equal({ accountId: 1, amount: 50 });

    expect(ticketService._ticketPaymentService.requestReceived).to.equal(true);
  });

  it('should calculate the correct total for the seats reserved and make a request to the seat reservation service', () => {
    const accountId = 1;
    const dadTypeRequests1 = new TicketTypeRequest('ADULT', 1);
    const momTypeRequests = new TicketTypeRequest('ADULT', 1);
    const childTypeRequests = new TicketTypeRequest('CHILD', 1);
    const infantTypeRequests = new TicketTypeRequest('INFANT', 1);

    ticketService.purchaseTickets(
      accountId,
      dadTypeRequests1,
      momTypeRequests,
      childTypeRequests,
      infantTypeRequests
    );

    // 2 adults, 1 child, 1 infant
    // 2 adults = 2 seats
    // 1 child = 1 seat
    // 1 infant = 0 seats, sits on lap
    // 2 + 1 + 0 = 3 seats total
    const expectedSeatsReserved =
      ticketService._seatReservationService.seatReservations[0];

    expect(expectedSeatsReserved).to.deep.equal({
      accountId: 1,
      totalSeats: 3,
    });
    expect(ticketService._seatReservationService.requestReceived).to.equal(
      true
    );
  });
});
