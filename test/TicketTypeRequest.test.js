import chai from 'chai';
import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest.js';

const { expect } = chai;

describe('TicketTypeRequest', () => {
  it('should correctly assign type and number of tickets', () => {
    const ticketRequest = new TicketTypeRequest('ADULT', 3);
    expect(ticketRequest.getTicketType()).to.equal('ADULT');
    expect(ticketRequest.getNoOfTickets()).to.equal(3);
  });

  it('should throw an error for invalid ticket types', () => {
    expect(() => new TicketTypeRequest('OAP', 3)).to.throw(TypeError);
  });

  it('should throw an error if number of tickets is not a number', () => {
    expect(() => new TicketTypeRequest('ADULT', '3')).to.throw(TypeError);
  });
});
