import sinon from 'sinon'
import { ticketRoutes } from '../../src/routes/tickets.js'

describe('ticketRoutes', () => {
  let app

  beforeEach(() => {
    app = {
      post: sinon.spy()
    }
  })

  it('should call post route', () => {
    ticketRoutes(app)

    sinon.assert.calledOnceWithMatch(app.post, '/tickets', Function)
  })
})
