import { assert, expect } from 'chai'
import sinon from 'sinon'
import { ticketsHandler } from '../../../src/lib/handlers/ticketsHandler.js'
import TicketService from '../../../src/lib/services/TicketService.js'
import TicketTypeRequest from '../../../src/lib/TicketTypeRequest.js'

describe('ticketsHandler', () => {
  let app
  let purchaseTicketsStub
  let testRequest

  beforeEach(() => {
    purchaseTicketsStub = sinon.stub(TicketService.prototype, 'purchaseTickets').returns({
      code: 200,
      message: 'success'
    })

    testRequest = {
      accountId: 1,
      ticketRequests: [{
        type: 'ADULT',
        noOfTickets: 1
      }
      ]
    }
  })

  afterEach(() => {
    purchaseTicketsStub.restore()
  })

  it('should call purchase tickets', () => {
    const result = ticketsHandler(testRequest)
    const expectedRequest = new TicketTypeRequest('ADULT', 1)

    sinon.assert.calledOnceWithMatch(purchaseTicketsStub, 1, expectedRequest)
    expect(result).to.be.deep.eq({
      code: 200,
      message: 'success'
    })
  })

  it('should throw an error if accountId is empty', () => {
    testRequest.accountId = undefined

    assert.throws(() => { ticketsHandler(testRequest) }, 'No Account ID provided')
  })

  it('should throw an error if request body is empty', () => {
    testRequest.ticketRequests = undefined

    assert.throws(() => { ticketsHandler(testRequest) }, 'No Ticket Requests provided')
  })
})
