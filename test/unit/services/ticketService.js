import initTicketService from '../../../src/services/ticketService.js'
const sandbox = sinon.createSandbox()

describe('services/ticketService', () => {
  let errorStub,
    loggerMock,
    logStub,
    makePaymentStub,
    reserveSeatStub

  const logString = 'Requesting ticket purchase'
  const error = new Error('@here cannae log in to gitlab help')
  const validAccountId = 23948
  const invalidAccountId = 'Jeff'
  const validTicketsRequested = {
    numberOfSeatsRequested: 20,
    totalPayment: 4000000
  }
  const invalidTicketsRequested = {
    numberOfSeatsRequested: 'Slurms McKenzie',
    totalPayment: false
  }

  beforeEach(() => {
    // stubs
    errorStub = sandbox.stub()
    logStub = sandbox.stub()
    makePaymentStub = sandbox.stub()
    reserveSeatStub = sandbox.stub()

    // mocks
    loggerMock = {
      error: errorStub,
      log: logStub
    }

    // configure stubs
    logStub
      .withArgs(logString)
      .returns(undefined)
    logStub
      .returns(Error('console.log called with unexpected parameters'))
    errorStub
      .withArgs(error)
      .returns(undefined)
    errorStub
      .throws(new Error('console.error called with unexpected parameters'))

    makePaymentStub
      .withArgs(validAccountId, validTicketsRequested.totalPayment)
      .returns(undefined)
    makePaymentStub
      .withArgs(invalidAccountId, invalidTicketsRequested.totalPayment)
      .throws(error)
    makePaymentStub
      .throws('makePaymentStub called with unexpected arguments')

    reserveSeatStub
      .withArgs(validAccountId, validTicketsRequested.numberOfSeatsRequested)
      .returns(undefined)
    reserveSeatStub
      .withArgs(invalidAccountId, invalidTicketsRequested.numberOfSeatsRequested)
      .throws(error)
    reserveSeatStub
      .throws('reserveSeatStub called with unexpected arguments')
  })

  afterEach(() => {
    sandbox.restore()
  })

  describe('valid call to service', () => {
    it('should not throw an error', () => {
      const ticketService = initTicketService({
        logger: loggerMock,
        makePayment: makePaymentStub,
        reserveSeat: reserveSeatStub
      })

      assert.doesNotThrow(
        () => ticketService.purchaseTickets({
          accountId: validAccountId,
          ticketsRequested: validTicketsRequested
        }),
        Error
      )
    })
    it('should call all expected functions', () => {
      const ticketService = initTicketService({
        logger: loggerMock,
        makePayment: makePaymentStub,
        reserveSeat: reserveSeatStub
      })

      const result = ticketService
        .purchaseTickets({
          accountId: validAccountId,
          ticketsRequested: validTicketsRequested
        })

      assert.isTrue(
        logStub.calledOnceWith(logString),
        'console.log not called with expected string'
      )

      assert.isTrue(
        makePaymentStub.calledOnceWith(
          validAccountId,
          validTicketsRequested.totalPayment
        ),
        'makePayment not called with expected arguments'
      )

      assert.isTrue(
        reserveSeatStub.calledOnceWith(
          validAccountId,
          validTicketsRequested.numberOfSeatsRequested
        ),
        'reserveSeats not called with expected arguments'
      )

      assert.isTrue(
        result,
        'service has not returned true'
      )

      assert.isTrue(
        errorStub.notCalled,
        'console.error unexpectedly called'
      )
    })
  })

  describe('invalid call to service', () => {
    it('should throw an error', () => {
      const ticketService = initTicketService({
        logger: loggerMock,
        makePayment: makePaymentStub,
        reserveSeat: reserveSeatStub
      })

      assert.throws(
        () => ticketService.purchaseTickets({
          accountId: invalidAccountId,
          ticketsRequested: invalidTicketsRequested
        }),
        error
      )
    })
  })
})
