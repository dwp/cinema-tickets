import chai from 'chai';
import TICKET_TYPES from '../src/pairtest/lib/TicketTypes.js';

const { expect } = chai;

describe('TicketTypes', () => {
  it('should have three types of tickets - ADULT, CHILD & INFANT', () => {
    expect(TICKET_TYPES).to.be.an('object');
    expect(Object.keys(TICKET_TYPES)).to.have.lengthOf(3);
    expect(TICKET_TYPES).to.have.property('ADULT');
    expect(TICKET_TYPES).to.have.property('CHILD');
    expect(TICKET_TYPES).to.have.property('INFANT');
  });
  it('the ticket prices are correct as default', () => {
    expect(TICKET_TYPES.ADULT).to.be.eql(20);
    expect(TICKET_TYPES.CHILD).to.be.eql(10);
    expect(TICKET_TYPES.INFANT).to.be.eql(0);
  });
});
