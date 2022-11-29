import { MAXIMUM_TICKET_LIMIT } from '../../constants.js'

export function accountIDValidator (accountID) {
  let error

  if (accountID !== 0 && !accountID) {
    error = 'AccountID is not provided!'
  } else if (typeof accountID !== 'number') {
    error = 'AccountID must be a number'
  } else if (accountID === 0) {
    error = 'AccountID cannot be zero!'
  }
  return error
}

export function requestValidator (request) {
  const errors = []
  // sum all of the requested tickets
  const totalNumberOfTickets = Object.values(request).reduce((a, b) => a + b)

  if (totalNumberOfTickets > MAXIMUM_TICKET_LIMIT) {
    errors.push(`Max number of tickets available to purchase at once is ${MAXIMUM_TICKET_LIMIT}!`)
  }

  if (request.CHILD === totalNumberOfTickets) {
    errors.push('Children must be accommodated by an adult!')
  } else if (request.ADULT === 0 && request.INFANT + request.CHILD > 1) {
    errors.push('Infants and children must be accommodated by an adult!')
  }

  if (request.INFANT > request.ADULT) {
    errors.push('Too many infants! Each infant needs an adult\'s lap to sit on.')
  }
  return errors
}
