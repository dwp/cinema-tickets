import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest';

describe('TicketTypeRequest', () => {
  describe('constructor', () => {
    it('should throw an error for invalid type', () => {
      expect(() => new TicketTypeRequest('INVALID_TYPE', 5)).toThrow(TypeError);
    });

    it('should throw an error for non-integer noOfTickets', () => {
      expect(() => new TicketTypeRequest('ADULT', 1.5)).toThrow(TypeError);
    });

    it('should set the type and noOfTickets for valid inputs', () => {
      const ticketTypeRequest = new TicketTypeRequest('CHILD', 3);

      expect(ticketTypeRequest.getTicketType()).toBe('CHILD');
      expect(ticketTypeRequest.getNoOfTickets()).toBe(3);
    });
  });

  describe('getNoOfTickets', () => {
    it('should return the correct number of tickets', () => {
      const ticketTypeRequest = new TicketTypeRequest('ADULT', 4);

      expect(ticketTypeRequest.getNoOfTickets()).toBe(4);
    });
  });

  describe('getTicketType', () => {
    it('should return the correct ticket type', () => {
      const ticketTypeRequest = new TicketTypeRequest('INFANT', 1);

      expect(ticketTypeRequest.getTicketType()).toBe('INFANT');
    });
  });
});
