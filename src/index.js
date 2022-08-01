const express = require('express')

const C = require('./constants')
const initServer = require('./server')

const app = express()

const handlers = {
  initHealthcheckHandler: require('./handlers/healthcheck')
}

const helpers = {
  initStartServer: require('./helpers/startServer')
}

const routing = require('./routes')

const startServer = initServer({
  app,
  C,
  handlers,
  helpers,
  routing
})

startServer()
