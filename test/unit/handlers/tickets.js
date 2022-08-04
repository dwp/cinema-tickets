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
  const error = new Error('MacBook melting...')

  beforeEach(() => {
    // variables
    validBody = {
      accountId: 39,
      tickets: {
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
    logStub = sinon.stub()
    logStub
      .withArgs(logString)
      .returns(undefined)
    logStub
      .returns(Error('console.log called with unexpected parameters'))
    errorStub = sinon.stub()
    errorStub
      .withArgs(error)
      .returns(undefined)
    errorStub
      .returns(Error('console.error called with unexpected parameters'))

    calculateSeatsToReserveStub
      .withArgs({ tickets: req.body.tickets })
      .returns(999)
      
    calculateTotalPaymentStub
      .withArgs({ tickets: req.body.tickets })
      .returns(9001)
    calculateTotalPaymentStub
      .throws(Error('calculateTotalPayment not called with exact arguments expected'))

    validateRequestStub
      .withArgs({
        accountId: req.body.accountId,
        ticketRequest: req.body.tickets
      })
      .returns(undefined)
    validateRequestStub
      .throws(Error('validateRequest failed'))

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

  describe('successful call to ticket handler', () => {
    it.only('should successfully call the expected functions', () => {
      const ticketHandler = initTicketHandler({
        C,
        logger: loggerMock,
        helpers: helpersMock,
        services: servicesMock
      })
      ticketHandler(req, resMock)
      
      assert.isTrue(
        statusStub.calledOnceWith(C.serverConfig.responseCodes.success),
        'res.status not called with 200'
        )
    })
  })
})