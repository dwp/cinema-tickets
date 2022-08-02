import initHealthcheckHandler from '../../../src/handlers/healthcheck.js'

const sandbox = sinon.createSandbox()

describe('handlers/healthcheck', () => {
  const req = {}

  let consoleMock,
    errorStub,
    logStub,
    resSendSpy,
    resStatusSpy,
    resMock

  const C = {
    routes: {
      healthcheck: {
        path: 'kcehchtlaeh',
        responseString: '😅'
      }
    },
    serverConfig: {
      responseCodes: {
        error: 1029384756,
        success: 5647382910
      }
    }
  }
  const logString = `Request to ${C.routes.healthcheck.path}`
  const error = new Error('blocked by zScaler')

  beforeEach(() => {
    // stub logging
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

    // mocks console
    consoleMock = {
      error: errorStub,
      log: logStub
    }

    resSendSpy = sinon.spy()
    resStatusSpy = sinon.stub()

    // mocks res object
    resMock = {
      send: resSendSpy,
      status: resStatusSpy
    }
    resStatusSpy.returns(resMock)
  })

  afterEach(() => {
    sandbox.restore()
  })

  describe('successful healthcheck', () => {
    it('should log that there has been a request and not log an error', async () => {
      const healthcheckHandler = initHealthcheckHandler({ C, logger: consoleMock })
      await healthcheckHandler({ req, res: resMock })

      assert.isTrue(
        logStub.calledOnceWith(`Request to ${C.routes.healthcheck.path}`),
        'console.log not called with expected request log message'
      )

      assert.isTrue(
        errorStub.notCalled,
        'error stub called unexpectedly'
      )
    })

    it('should return a 200 response and response string', async () => {
      const healthcheckHandler = initHealthcheckHandler({ C, logger: consoleMock })
      await healthcheckHandler({ req, res: resMock })

      assert.isTrue(
        resStatusSpy.calledOnceWith(C.serverConfig.responseCodes.success),
        'res.status not called once with success code'
      )

      assert.isTrue(
        resSendSpy.calledOnceWith(C.routes.healthcheck.responseString),
        'res.send not called with response string'
      )
    })
  })
})
