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
