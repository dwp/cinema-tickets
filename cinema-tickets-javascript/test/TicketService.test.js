import TicketService from '../src/pairtest/TicketService';
import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException';

import SeatReservationService from '../src/thirdparty/seatbooking/SeatReservationService';
import TicketPaymentService from '../src/thirdparty/paymentgateway/TicketPaymentService';

describe('TicketService', () => {
  let ticketService;

  beforeEach(() => {
    jest.clearAllMocks();
    ticketService = new TicketService();
  });

  describe('purchaseTickets', () => {

    it('should throw an error if accountId is a string', () => {
      const accountId = '123';
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 5 },
      ];


      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).toThrow(new InvalidPurchaseException('accountId must be an integer'));
    });

    it('should throw an error if accountId is not valid', () => {
      const accountId = 0;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 5 },
      ];

      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).toThrow(new InvalidPurchaseException('invalid accountId'));
    });

    it('should throw an error if ticket type is not valid', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'PENSIONER', noOfTickets: 5 },
      ];

      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).toThrow(new TypeError('type must be ADULT, CHILD, or INFANT'));
    });

    it('should throw an error if noOfTickets is less than 1', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 0 },
      ];

      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).toThrow(new InvalidPurchaseException('no of tickets must be greater than or equal to 1'));
    });


    it('should throw an error if noOfTickets is greater than 20', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 21 },
      ];

      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).toThrow(new InvalidPurchaseException('no of tickets must be less than or equal to 20'));
    });

    it('should throw an error if total number of child and infant tickets is greater than the number of adult tickets', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 2 },
        { type: 'CHILD', noOfTickets: 2 },
        { type: 'INFANT', noOfTickets: 1 },
      ];

      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).toThrow(new InvalidPurchaseException('total number of child and infant tickets must be less than or equal to the number of adult tickets'));
    });

    it('should not throw an error if total number of child and infant tickets is equal to the number of adult tickets', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 2 },
        { type: 'CHILD', noOfTickets: 2 },
        { type: 'INFANT', noOfTickets: 0 },
      ];

      expect(() => ticketService.purchaseTickets(accountId, ...ticketTypeRequests)).not.toThrow();
    });

    it('should call reserveSeat and payTicket with the correct values', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 2 },
        { type: 'CHILD', noOfTickets: 1 },
      ];

      const ticketService = new TicketService();

      const reserveSeatMock = jest.spyOn(SeatReservationService.prototype, 'reserveSeat');
      const payTicketMock = jest.spyOn(TicketPaymentService.prototype, 'makePayment');

      ticketService.purchaseTickets(accountId, ...ticketTypeRequests);

      expect(reserveSeatMock).toHaveBeenCalledWith(accountId, 3);
      expect(payTicketMock).toHaveBeenCalledWith(accountId, 50);
    });

    it('should not count infant tickets towards the total number of seats', () => {
      const accountId = 123;
      const ticketTypeRequests = [
        { type: 'ADULT', noOfTickets: 2 },
        { type: 'INFANT', noOfTickets: 1 },
      ];

      const ticketService = new TicketService();

      const reserveSeatMock = jest.spyOn(SeatReservationService.prototype, 'reserveSeat');
      const payTicketMock = jest.spyOn(TicketPaymentService.prototype, 'makePayment');

      ticketService.purchaseTickets(accountId, ...ticketTypeRequests);

      expect(reserveSeatMock).toHaveBeenCalledWith(accountId, 2);
      expect(payTicketMock).toHaveBeenCalledWith(accountId, 40);
    });
  });
});
