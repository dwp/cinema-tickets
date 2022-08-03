import initValidateRequest from '../../../src/helpers/validateRequest.js'
import C from '../../../src/constants/index.js'

describe('helpers/validateRequest', () => {
  const validateRequest = initValidateRequest({ C })
  const errors = C.tickets.responses.error

  describe('invalid requests', () => {
    it('(noAccountId): should reject a request with no account ID', () => {
      const ticketRequest = {}
      assert.throws(
        () => validateRequest({ ticketRequest }),
        Error,
        errors.noAccountId
      )
    })
    it('(invalidAccountId): should reject an invalid account ID', () => {
      const accountId = '1'
      const ticketRequest = {}
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        `${errors.invalidAccountId},${errors.invalidTicketType},${errors.invalidTicketNumber}`
      )
      // sanity check against false negatives
      assert.doesNotThrow(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        `${errors.insufficientAdultTickets}`
      )
    })
    it('(noTicketsRequested): should reject a valid account ID and no tickets requested', () => {
      const accountId = 1
      const ticketRequest = { adult: 0, child: 0, infant: 0 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        errors.noTicketsRequested
      )
    })
    it('(invalidTicketType): should reject a valid account ID and an empty ticket request', () => {
      const accountId = 1
      const ticketRequest = {}
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        `${errors.invalidTicketType},${errors.invalidTicketNumber}`
      )
    })
    it('(invalidTicketType): should reject a valid account ID and an invalid ticket type request', () => {
      const accountId = 1
      const ticketRequest = { adult: 3, child: 2, infant: 1, emotionalSupportChinchilla: 1 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        errors.invalidTicketType
      )
    })
    it('(invalidTicketNumber): should reject a valid account ID and an invalid ticket number request', () => {
      const accountId = 1
      const ticketRequest = { adult: 3, child: 2, infant: false }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        errors.invalidTicketNumber
      )
    })
    it('(maxTicketsExceeded): should reject a valid account ID and an invalid number of tickets', () => {
      const accountId = 1
      const ticketRequest = { adult: 3, child: 80000, infant: 1 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        errors.maxTicketsExceeded
      )
    })
    it('(insufficientAdultTickets): should reject a valid account ID and more infant than adult tickets requested', () => {
      const accountId = 1
      const ticketRequest = { adult: 1, child: 0, infant: 2 }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        errors.insufficientAdultTickets
      )
    })
    it('(multiple errors): should handle multiple errors', () => {
      const accountId = 'CAT'
      const ticketRequest = { adult: -3, infant: 4000000, courgettes: true }
      assert.throws(
        () => validateRequest({ accountId, ticketRequest }),
        Error,
        `${errors.invalidAccountId},${errors.invalidTicketType},${errors.invalidTicketNumber},${errors.insufficientAdultTickets}`
      )
    })
  })

  describe('valid requests', () => {
    it('should accept a valid account ID and valid number of tickets', () => {
      const accountId = 39
      const ticketRequest = { adult: 3, child: 7, infant: 3 }

      assert.doesNotThrow(
        () => validateRequest({ accountId, ticketRequest }),
        Error
      )
    })
  })
})
