import * as sinon from 'sinon'
import { assert } from 'chai'

import TicketPaymentService from '../../../src/thirdparty/paymentgateway/TicketPaymentService.js'
import TicketService from '../../../src/lib/services/TicketService.js'
import TicketTypeRequest from '../../../src/lib/TicketTypeRequest.js'

describe('TicketService', () => {
  let makePaymentStub
  let ticketService

  beforeEach(() => {
    makePaymentStub = sinon.stub(TicketPaymentService.prototype, 'makePayment').returns(true)
    ticketService = new TicketService()
  })

  afterEach(() => {
    makePaymentStub.restore()
  })

  context('purchaseTickets', () => {
    it('should call makePayment method of ticketPaymentService', () => {
      const ticketRequest= new TicketTypeRequest('ADULT', 4)
      ticketService.purchaseTickets(1, ticketRequest)
      sinon.assert.calledOnceWithExactly(makePaymentStub, 1, 80)
    })

    it('should throw an error if accountID is invalid', () => {
      assert.throws(() => { ticketService.purchaseTickets(0, 0) }, 'AccountID cannot be zero!')
    })

    it('should throw an error if given an invalid request', () => {
      const ticketRequest = new TicketTypeRequest('INFANT', 9)
      const ticketRequestTwo = new TicketTypeRequest('ADULT', 2)
      assert.throws(() => { ticketService.purchaseTickets(1, ticketRequest, ticketRequestTwo) }, 'Too many infants! Each infant needs an adult\'s lap to sit on.')
    })
  })
})
