import { assert } from 'chai'
import TicketTypeRequest from '../../src/lib/TicketTypeRequest.js'

describe('TicketTypeRequest', () => {
  context('constructor', () => {
    it('should throw an error if invalid type is passed', () => {
      assert.throws(() => { new TicketTypeRequest('ELDER', 10) }, 'type must be ADULT, CHILD, or INFANT')
    })

    it('should throw an error if a non-integer is passed', () => {
      assert.throws(() => { new TicketTypeRequest('ADULT', 3.14) }, 'noOfTickets must be an integer')
    })

    it('should throw an error if noOfTickers is greater than 20', () => {
      assert.throws(() => { new TicketTypeRequest('ADULT', 21) }, 'max noOfTickets is 20!')
    })
  })
})
