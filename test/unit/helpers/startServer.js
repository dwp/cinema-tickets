import initStartServer from '../../../src/helpers/startServer.js'
const sandbox = sinon.createSandbox()

describe('helpers/startServer', () => {
  let appMock,
    consoleMock,
    errorStub,
    exitSpy,
    listenStub,
    logStub,
    startServer,
    testErrorState

  const host = 'nickholvast.com'
  const port = 3000000
  const startMsg = 'Windows 95 start sound goes here'
  const logMsg = `${startMsg} ${host}:${port}`

  const error = new Error('Goat in the server')

  beforeEach(() => {
    // stub app.listen() function
    listenStub = sandbox.stub()
    listenStub.withArgs(port, host).returns(undefined)
    listenStub.returns(Error('app.listen called with unexpected parameters'))

    // stub logging
    logStub = sandbox.stub()
    logStub.withArgs(logMsg).returns(undefined)
    logStub.returns(Error('console.log called with unexpected parameters'))
    errorStub = sandbox.stub()
    errorStub.withArgs(error).returns(undefined)
    errorStub.returns(Error('console.error called with unexpected parameters'))

    // mocks express app
    appMock = {
      listen: listenStub
    }

    // mocks console
    consoleMock = {
      error: errorStub,
      log: logStub
    }

    // spy on process.exit
    exitSpy = sandbox.spy()

    // default to start server with no error
    startServer = initStartServer({
      app: appMock,
      exit: exitSpy,
      host,
      logger: consoleMock,
      port,
      startMsg
    })

    // set testErrorState to true in error state tests
    if (testErrorState) {
      listenStub
        .withArgs(port, host)
        .throws(error)
    }

    startServer()
  })

  afterEach(() => {
    sandbox.restore()
  })

  describe('successful server start', () => {
    it('should call app.listen()', () => {
      assert.isTrue(
        listenStub.calledOnceWith(port, host),
        'app.listen not called with expected parameters'
      )
    })
    it('should log the start message and not log an error', () => {
      assert.isTrue(
        logStub.calledOnceWith(logMsg),
        'console.log not called once with expected log message'
      )

      assert.isTrue(errorStub.notCalled)
    })
    it('should not exit', () => {
      assert.isTrue(
        exitSpy.notCalled,
        'process.exit() called when not expected'
      )
    })
  })

  describe('server start with error', () => {
    it('should handle an error when the server fails to start successfully', () => {
      // overrides listenStub to throw error
      testErrorState = true

      assert.isTrue(
        listenStub.calledOnceWith(port, host),
        'app.listen not called with expected parameters'
      )
    })

    it('should call console.error and not call console.log', () => {
      testErrorState = true

      assert.isTrue(logStub.notCalled)

      assert.isTrue(
        errorStub.calledOnceWith(error),
        'logger.error not called once with expected error'
      )
    })

    it('should call process.exit with code \'1\'', () => {
      testErrorState = true

      assert.isTrue(
        exitSpy.calledOnceWith(1),
        'process.exit() not called when expected'
      )
    })
  })
})
