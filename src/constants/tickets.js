const ADULT = 'ADULT'
const CHILD = 'CHILD'
const INFANT = 'INFANT'

export default {
  allowedTypes: [ADULT, CHILD, INFANT],
  responses: {
    error: {
      adultTicketRequired: 'Child and infant tickets must be purchased with an adult ticket',
      insufficientAdultTickets: 'The number of infant tickets must not exceed the number of adult tickets',
      invalidAccountId: 'Invalid account ID',
      invalidTicketNumber: 'Invalid ticket number format - number of tickets must be an integer',
      invalidTicketType: 'Invalid ticket type: valid types are "ADULT" "CHILD" "INFANT"',
      maxTicketsExceeded: 'Maximum number of tickets exceeded',
      noAccountId: 'No account ID provided',
      noTicketsRequested: 'Number of tickets requested must be greater than 0'
    }
  },
  type: {
    adult: {
      price: 20,
      type: ADULT,
      usesSeat: true
    },
    child: {
      price: 10,
      type: CHILD,
      usesSeat: true
    },
    infant: {
      price: 0,
      type: INFANT,
      usesSeat: false
    }
  }
}
