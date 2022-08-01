const express = require('express')

const C = require('./constants')
const initServer = require('./server')

// const logger = console
const exit = process.exit

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
  exit,
  handlers,
  helpers,
  logger: console,
  routing
})

startServer()
