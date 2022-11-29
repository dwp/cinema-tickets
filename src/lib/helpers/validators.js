export function accountIDValidator (accountID) {
  const errors = []

  if (accountID !== 0 && !accountID) {
    errors.push('AccountID is not provided!')
  } else if (typeof accountID !== 'number') {
    errors.push('AccountID must be a number')
  } else if (accountID === 0) {
    errors.push('AccountID cannot be zero!')
  }
  return errors
}

export function requestValidator (request) {
  const errors = []
  const totalNumberOfTickets = Object.values(request).reduce((a, b) => a + b);

  if(totalNumberOfTickets > 20) {
    errors.push('Max number of tickets available to purchase at once is 20!')
  }

  if (request['CHILD'] === totalNumberOfTickets) {
    errors.push('Children must be accommodated by an adult!')
  } else if (request['ADULT'] === 0 && request['INFANT'] + request['CHILD'] > 1) {
    errors.push('Infants and children must be accommodated by an adult!')
  }

  if(request['INFANT'] > request['ADULT']) {
    errors.push('Too many infants! Each infant needs an adult\'s lap to sit on.')
  }
  return errors
}