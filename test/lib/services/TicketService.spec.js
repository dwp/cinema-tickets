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
      let ticketRequestOne = new TicketTypeRequest('ADULT', 4);
      ticketService.purchaseTickets(1, ticketRequestOne)
      sinon.assert.calledOnceWithExactly(makePaymentStub, 1, 0)
    })

    it('should throw an error if accountID is invalid', () => {
      assert.throws(() => { ticketService.purchaseTickets(0, 0) }, 'AccountID cannot be zero!')
    })
  })
})
