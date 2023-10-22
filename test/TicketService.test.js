/* eslint-disable no-underscore-dangle */
/* eslint-disable no-unused-expressions */
import chai from 'chai';
import sinon from 'sinon';
import TicketService from '../src/pairtest/TicketService.js';
import TicketPaymentService from '../src/thirdparty/paymentgateway/TicketPaymentService.js';
import SeatReservationService from '../src/thirdparty/seatbooking/SeatReservationService.js';
import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException.js';
import Validator from '../src/pairtest/lib/Validator.js';
import Calculator from '../src/pairtest/lib/Calcuator.js';

const { expect } = chai;

describe('TicketService', () => {
  let ticketService;
  let mockPaymentService;
  let mockReservationService;
  let sandbox;

  beforeEach(() => {
    sandbox = sinon.createSandbox();
    mockPaymentService = sinon.createStubInstance(TicketPaymentService);
    mockReservationService = sinon.createStubInstance(SeatReservationService);
    ticketService = new TicketService(mockPaymentService, mockReservationService);
  });

  afterEach(() => {
    sandbox.restore();
  });

  describe('purchaseTickets', () => {
    it('should throw error for invalid accountId or ticket requests', () => {
      expect(() => ticketService.purchaseTickets()).to.throw('Invalid accountId or ticket requests');
    });

    it('should throw InvalidPurchaseException when Validator detects an invalid ticket request', () => {
      sandbox.stub(Validator, 'isTicketCountValid').returns(false);
      expect(() => ticketService.purchaseTickets('account123', 'type1')).to.throw(InvalidPurchaseException);
    });

    it('should throw InvalidPurchaseException when there isnt an accompanying adult', () => {
      sandbox.stub(Validator, 'isTicketCountValid').returns(true);
      sandbox.stub(Validator, 'hasAccompanyingAdult').returns(false);
      expect(() => ticketService.purchaseTickets('account123', 'type1')).to.throw(InvalidPurchaseException);
    });

    it('should make payment and reserve seats when requests are valid', () => {
      sandbox.stub(Validator, 'isTicketCountValid').returns(true);
      sandbox.stub(Validator, 'hasAccompanyingAdult').returns(true);
      sandbox.stub(Calculator, 'getTotalCost').returns(100);
      sandbox.stub(Calculator, 'getTotalSeats').returns(2);

      ticketService.purchaseTickets('account123', 'type1', 'type2');

      sinon.assert.calledWith(mockPaymentService.makePayment, 'account123', 100);
      sinon.assert.calledWith(mockReservationService.reserveSeat, 'account123', 2);
    });

    it('should throw error if payment fails', () => {
      sandbox.stub(Validator, 'isTicketCountValid').returns(true);
      sandbox.stub(Validator, 'hasAccompanyingAdult').returns(true);
      sandbox.stub(Calculator, 'getTotalCost').returns(100);
      sandbox.stub(Calculator, 'getTotalSeats').returns(2);
      mockPaymentService.makePayment.throws(new Error('Failed to make payment.'));

      expect(() => ticketService.purchaseTickets('account123', 'type1', 'type2')).to.throw('Failed to make payment.');
    });

    it('should throw error if seat reservation fails', () => {
      sandbox.stub(Validator, 'isTicketCountValid').returns(true);
      sandbox.stub(Validator, 'hasAccompanyingAdult').returns(true);
      sandbox.stub(Calculator, 'getTotalCost').returns(100);
      sandbox.stub(Calculator, 'getTotalSeats').returns(2);
      mockReservationService.reserveSeat.throws(new Error('Failed to reserve seats.'));

      expect(() => ticketService.purchaseTickets('account123', 'type1', 'type2')).to.throw('Failed to reserve seats.');
    });

    it('should log error and re-throw if there is an error during purchase', () => {
      const consoleErrorStub = sandbox.stub(console, 'error');
      sandbox.stub(Validator, 'isTicketCountValid').throws(new Error('Random Error'));

      expect(() => ticketService.purchaseTickets('account123', 'type1')).to.throw('Random Error');
      sinon.assert.calledWith(consoleErrorStub, 'Error while purchasing tickets');
    });
  });

  describe('_makePayment', () => {
    it('should make payment when called', () => {
      ticketService._makePayment('account123', 100);
      sinon.assert.calledWith(mockPaymentService.makePayment, 'account123', 100);
    });

    it('should log error and throw when payment service throws an error', () => {
      const consoleErrorStub = sandbox.stub(console, 'error');
      mockPaymentService.makePayment.throws(new Error('Payment Error'));

      expect(() => ticketService._makePayment('account123', 100)).to.throw('Failed to make payment.');
      sinon.assert.calledWith(consoleErrorStub, 'Error while making payment');
    });
  });

  describe('_reserveSeats', () => {
    it('should reserve seats when called', () => {
      ticketService._reserveSeats('account123', 2);
      sinon.assert.calledWith(mockReservationService.reserveSeat, 'account123', 2);
    });

    it('should log error and throw when reservation service throws an error', () => {
      const consoleErrorStub = sandbox.stub(console, 'error');
      mockReservationService.reserveSeat.throws(new Error('Reservation Error'));

      expect(() => ticketService._reserveSeats('account123', 2)).to.throw('Failed to reserve seats.');
      sinon.assert.calledWith(consoleErrorStub, 'Error while reserving seats');
    });
  });
});
