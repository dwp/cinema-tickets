import initCalculateTotalPayment from '../../../src/helpers/calculateTotalPayment.js'

describe('helpers/calculateTotalPayment', () => {
  const C = {
    tickets: {
      type: {
        adult: {
          price: 20
        },
        child: {
          price: 10
        },
        infant: {
          price: 0
        }
      }
    }
  }

  const calculateTotalPayment = initCalculateTotalPayment({ C })

  describe('invalid requests', () => {
    it('should reject an empty request', () => {
      const tickets = {}
      assert.throws(
        () => calculateTotalPayment({ tickets }),
        Error,
        /Invalid ticket request/
      )
    })
  })

  describe('valid requests', () => {
    it('should calculate requests for only adults', () => {
      const tickets = {
        adult: 5,
        child: 0,
        infant: 0
      }
      const totalPayment = calculateTotalPayment({ tickets })
      const expectedPayment = C.tickets.type.adult.price * tickets.adult
      assert.equal(
        expectedPayment,
        totalPayment,
        'Total payment does not equal expected payment'
      )
    })

    it('should calculate requests for adults and children', () => {
      const tickets = {
        adult: 1,
        child: 19,
        infant: 0
      }
      const totalPayment = calculateTotalPayment({ tickets })
      const expectedPayment = (
        (C.tickets.type.adult.price * tickets.adult) +
        (C.tickets.type.child.price * tickets.child)
      )
      assert.equal(
        expectedPayment,
        totalPayment,
        'Total payment does not equal expected payment'
      )
    })

    it('should calculate requests for adults, children and infants', () => {
      const tickets = {
        adult: 10,
        child: 10,
        infant: 1000
      }
      const totalPayment = calculateTotalPayment({ tickets })
      const expectedPayment = (
        (C.tickets.type.adult.price * tickets.adult) +
        (C.tickets.type.child.price * tickets.child)
      )
      assert.equal(
        expectedPayment,
        totalPayment,
        'Total payment does not equal expected payment'
      )
    })
  })
})
