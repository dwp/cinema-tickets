const initStartServer = require('../../../src/helpers/startServer')

const sandbox = sinon.createSandbox()

describe('helpers/startServer', () => {
  let appMock,
    listenStub,
    startServer

  const host = 'nickholvast.com'
  const port = 3000000
  const startMsg = 'Windows 95 start sound goes here'

  beforeEach(() => {
    // stub app.listen() function
    listenStub = sandbox.stub()
    listenStub.withArgs(port, host).returns(undefined)
    listenStub.returns(Error('app.listen called with unexpected parameters'))

    // mocks express app
    appMock = {
      listen: listenStub
    }

    startServer = initStartServer({
      app: appMock,
      host,
      port,
      startMsg
    })

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
  })
})
