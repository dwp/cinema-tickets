import * as sinon from 'sinon'
import { assert, expect } from 'chai'

import TicketPaymentService from '../../../src/thirdparty/paymentgateway/TicketPaymentService.js'
import SeatReservationService from '../../../src/thirdparty/seatbooking/SeatReservationService.js'

import TicketService from '../../../src/lib/services/TicketService.js'
import TicketTypeRequest from '../../../src/lib/TicketTypeRequest.js'

describe('TicketService', () => {
  let makePaymentStub
  let reserveSeatStub
  let ticketService

  beforeEach(() => {
    makePaymentStub = sinon.stub(TicketPaymentService.prototype, 'makePayment').returns(true)
    reserveSeatStub = sinon.stub(SeatReservationService.prototype, 'reserveSeat').returns(true)
    ticketService = new TicketService()
  })

  afterEach(() => {
    [makePaymentStub, reserveSeatStub].forEach(stub => stub.restore())
  })

  context('purchaseTickets', () => {
    let ticketRequest
    it('should call makePayment method of ticketPaymentService', () => {
      ticketRequest = new TicketTypeRequest('ADULT', 4)
      ticketService.purchaseTickets(1, ticketRequest)
      sinon.assert.calledOnceWithExactly(makePaymentStub, 1, 80)
    })

    it('should call reserveSeat', () => {
      ticketRequest = new TicketTypeRequest('ADULT', 4)
      ticketService.purchaseTickets(1, ticketRequest)
      sinon.assert.calledOnceWithExactly(reserveSeatStub, 1, 4)
    })

    it('should return success with message', () => {
      ticketRequest = new TicketTypeRequest('ADULT', 4)
      let result = ticketService.purchaseTickets(1, ticketRequest)
      expect(result).to.be.deep.eq({
        code: 200,
        message: "You have successfully reserved 4 seats for £80."
      })
    })

    it('should throw an error if accountID is invalid', () => {
      assert.throws(() => { ticketService.purchaseTickets(0, 0) }, 'AccountID cannot be zero!')
    })

    it('should throw an error if given an invalid request', () => {
      const ticketRequests = [new TicketTypeRequest('INFANT', 9), new TicketTypeRequest('ADULT', 2)]
      assert.throws(() => { ticketService.purchaseTickets(1, ...ticketRequests) }, 'Too many infants! Each infant needs an adult\'s lap to sit on.')
    })
  })
})
