// SUT
import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest';

describe('TicketTypeRequest tests', () => {
  it('should not accept any other ticket types other than Adult, Child, Infant', () => {
    expect(() => { new TicketTypeRequest('BAR', 8); }).toThrow(TypeError);
  });

  it('should not allow for a non-integer for the number of specific ticket types', () => {
    expect(() => { new TicketTypeRequest('CHILD', 'bar'); }).toThrow(TypeError);
  });

  it('should not allow for a non-integer for the number of specific ticket type00s', () => {
    expect(() => { new TicketTypeRequest('INFANT', 0.1); }).toThrow(TypeError);
  });

  it('should not allow for any negative ticket numbers to be purchased', () => {
    expect(() => { new TicketTypeRequest('ADULT', -3); }).toThrow(TypeError);
  });

  it('should set the type given valid data', () => {
    const ticketTypeRequest = new TicketTypeRequest('ADULT', 10);

    expect(ticketTypeRequest.getTicketType()).toEqual('ADULT');
  });

  it('should set the number of tickets given valid data', () => {
    const ticketTypeRequest = new TicketTypeRequest('INFANT', 7);

    expect(ticketTypeRequest.getNoOfTickets()).toEqual(7);
  });
});
