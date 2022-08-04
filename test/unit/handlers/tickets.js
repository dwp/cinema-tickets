import initTicketHandler from '../../../src/handlers/tickets.js'
import C from '../../../src/constants/index.js'

const sandbox = sinon.createSandbox()

describe('handlers/tickets', () => {
  let calculateSeatsToReserveStub,
    calculateTotalPaymentStub,
    errorStub,
    helpersMock,
    invalidBody,
    loggerMock,
    logStub,
    purchaseTicketsStub,
    resMock,
    req,
    servicesMock,
    statusStub,
    sendSpy,
    validateRequestStub,
    validBody

  const logString = `Request to ${C.routes.tickets.path}`
  const errorMessage = 'MacBook melting...'
  const error = new Error(errorMessage)

  beforeEach(() => {
    // variables
    validBody = {
      accountId: 39,
      ticketsRequested: {
        adult: 2,
        child: 1,
        infant: 1
      }
    }

    invalidBody = {
      accountId: 'Bruno',
      tickets: {
        udalt: 45,
        john: 'ok'
      }
    }

    // override in error tests with invalidBody
    req = { body: validBody }

    // spies
    sendSpy = sandbox.spy()

    // stubs
    errorStub = sandbox.stub()
    logStub = sandbox.stub()
    calculateSeatsToReserveStub = sandbox.stub()
    calculateTotalPaymentStub = sandbox.stub()
    purchaseTicketsStub = sandbox.stub()
    statusStub = sandbox.stub()
    validateRequestStub = sandbox.stub()

    // mocks
    loggerMock = {
      error: errorStub,
      log: logStub
    }

    resMock = {
      send: sendSpy,
      status: statusStub
    }

    statusStub.returns(resMock)

    helpersMock = {
      calculateSeatsToReserve: calculateSeatsToReserveStub,
      calculateTotalPayment: calculateTotalPaymentStub,
      validateRequest: validateRequestStub
    }

    servicesMock = {
      ticketService: {
        purchaseTickets: purchaseTicketsStub
      }
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
      .returns(Error('console.error called with unexpected parameters'))

    calculateSeatsToReserveStub
      .withArgs({ tickets: req.body.ticketsRequested })
      .returns(999)

    calculateTotalPaymentStub
      .withArgs({ tickets: req.body.ticketsRequested })
      .returns(9001)
    calculateTotalPaymentStub
      .throws(Error('calculateTotalPayment not called with exact arguments expected'))

    validateRequestStub
      .withArgs({
        accountId: req.body.accountId,
        ticketRequest: req.body.ticketsRequested
      })
      .returns(undefined)
    validateRequestStub
      .throws([error])

    purchaseTicketsStub
      .withArgs({
        accountId: req.body.accountId,
        ticketsRequested: {
          numberOfSeatsRequested: 999, // returned from calculateSeatsToReserveStub
          totalPayment: 9001 // returned from calculateTotalPaymentStub
        }
      })
      .returns(true)
    purchaseTicketsStub
      .throws(Error('ticketService.purchaseTickets not called with correct arguments'))
  })

  afterEach(() => {
    sandbox.restore()
  })

  describe('valid call to ticket handler', () => {
    it('should not throw an error', () => {
      const ticketHandler = initTicketHandler({
        C,
        logger: loggerMock,
        helpers: helpersMock,
        services: servicesMock
      })

      assert.doesNotThrow(
        () => ticketHandler(req, resMock),
        Error
      )
    })

    it('should successfully call the expected functions', () => {
      const ticketHandler = initTicketHandler({
        C,
        logger: loggerMock,
        helpers: helpersMock,
        services: servicesMock
      })
      ticketHandler(req, resMock)

      assert.isTrue(
        logStub.calledOnceWith(logString),
        'console.log not called with request information'
      )

      assert.isTrue(
        validateRequestStub.calledOnceWith({
          accountId: req.body.accountId,
          ticketRequest: req.body.ticketsRequested
        }),
        'validateRequest not called once with expected req.body params'
      )

      assert.isTrue(
        calculateSeatsToReserveStub.calledOnceWith({ tickets: req.body.ticketsRequested }),
        'calculateSeatsToReserve not called once with correct number of tickets'
      )

      assert.isTrue(
        calculateTotalPaymentStub.calledOnceWith({ tickets: req.body.ticketsRequested }),
        'calculateTotalPayment not called once with correct number of tickets'
      )

      assert.isTrue(
        purchaseTicketsStub.calledOnceWith({
          accountId: req.body.accountId,
          ticketsRequested: {
            numberOfSeatsRequested: 999, // returned from calculateSeatsToReserveStub
            totalPayment: 9001 // returned from calculateTotalPaymentStub
          }
        }),
        'ticketService.purchaseTickets not called once with arguments returned from helper functions'
      )

      assert.isTrue(
        statusStub.calledOnceWith(C.serverConfig.responseCodes.success),
        'res.status not called with 200'
      )

      assert.isTrue(
        sendSpy.calledOnceWith({
          success: true,
          numberOfSeatsRequested: 999,
          totalPayment: 9001
        }),
        'res.send not called with correct arguments'
      )

      assert.isTrue(
        errorStub.notCalled,
        'console.error called unexpectedly'
      )
    })
  })

  describe('invalid call to ticket handler', () => {
    it('should throw an error', () => {
      req = { body: invalidBody }
      const ticketHandler = initTicketHandler({
        C,
        logger: loggerMock,
        helpers: helpersMock,
        services: servicesMock
      })

      ticketHandler(req, resMock)

      assert.isTrue(
        logStub.calledOnceWith(logString),
        'console.log not called with request details'
      )

      assert.isTrue(
        errorStub.calledOnceWith('Returning error to client'),
        'console.error not called with error message'
      )

      assert.isTrue(
        statusStub.calledOnceWith(C.serverConfig.responseCodes.error),
        'res.status not called with 500'
      )

      assert.isTrue(
        sendSpy.calledOnceWith({
          success: false,
          statusCode: C.serverConfig.responseCodes.error,
          errors: [errorMessage]
        }),
        'res.send not called with error'
      )
    })
  })
})
