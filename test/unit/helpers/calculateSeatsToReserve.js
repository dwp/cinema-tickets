import initCalculateSeatsToReserve from '../../../src/helpers/calculateSeatsToReserve.js'
import C from '../../../src/constants/index.js'

describe('helpers/calculateSeatsToReserve', () => {
  const calculateSeatsToReserve = initCalculateSeatsToReserve({ C })

  it('should reserve seats for a valid request', () => {
    const tickets = { adult: 5, child: 5, infant: 3 }
    const expectedResult = 10
    const result = calculateSeatsToReserve({ tickets })
    assert.equal(
      expectedResult,
      result,
      'Unexpected number of seats allocated'
    )
  })
})
