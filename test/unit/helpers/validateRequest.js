import initValidateRequest from '../../../src/helpers/validateRequest.js'
import C from '../../../src/constants/index.js'

describe('helpers/validateRequest', () => {
  const validateRequest = initValidateRequest({ C })

  describe('invalid requests', () => {
    it('(invalidAccountId): should reject an invalid account ID', () => {
      const accountId = '1'
      const ticketRequest = {}
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.invalidAccountId
      )
    })
    it('(noAccountId): should reject a request with no account ID', () => {
      const ticketRequest = {}
      assert.throws(
        () => validateRequest({ ticketRequest }),
        Error,
        C.tickets.responses.error.noAccountId
      )
    })
    it('(invalidTicketType): should reject a valid account ID and an empty ticket request', () => {
      const accountId = 1
      const ticketRequest = {}
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.invalidTicketType
      )
    })
    it('(invalidTicketType): should reject a valid account ID and an invalid ticket type request', () => {
      const accountId = 1
      const ticketRequest = { adult: 3, child: 2, infant: 1, emotionalSupportChinchilla: 1 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.invalidTicketType
      )
    })
    it('(invalidTicketNumber): should reject a valid account ID and an invalid ticket number request', () => {
      const accountId = 1
      const ticketRequest = { adult: 3, child: 2, infant: false }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.invalidTicketNumber
      )
    })
    it('(maxTicketsExceeded): should reject a valid account ID and an invalid number of tickets', () => {
      const accountId = 1
      const ticketRequest = { adult: 3, child: 8, infant: 10 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.maxTicketsExceeded
      )
    })
    it('(noTicketsRequested): should reject a valid account ID and no tickets requested', () => {
      const accountId = 1
      const ticketRequest = { adult: 0, child: 0, infant: 0 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.noTicketsRequested
      )
    })
    it('(insufficientAdultTickets): should reject a valid account ID and more infant than adult tickets requested', () => {
      const accountId = 1
      const ticketRequest = { adult: 1, child: 0, infant: 2 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        C.tickets.responses.error.noTicketsRequested
      )
    })
  })

  describe('valid requests', () => {
    ('it should accept a valid account ID and valid number of tickets', () => {
      const accountId = 39
      const ticketRequest = { adult: 3, child: 7, infant: 3 }

      const requestIsValid = validateRequest({ accountId, ticketRequest })

      assert.true(
        requestIsValid,
        'Request unexpectedly not valid'
      )
    })
  })
})