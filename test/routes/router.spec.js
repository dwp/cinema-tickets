import { expect } from 'chai'
import { router } from '../../src/routes/routes.js'

describe('router', () => {
  it('should be a function', () => {
    expect(router).to.be.an.instanceOf(Function)
  })
})
