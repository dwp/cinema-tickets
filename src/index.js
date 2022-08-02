// import { createRequire } from 'module'
// const require = createRequire(import.meta.url)

import express from 'express'

import C from './constants/index.js'
import initServer from './server.js'

const logger = console
const exit = process.exit

const app = express()

import initHealthcheckHandler from './handlers/healthcheck.js'

import initStartServer from './helpers/startServer.js'

// third party provided 'services'
import TicketPaymentService from './thirdparty/paymentgateway/TicketPaymentService.js'
const makePayment = new TicketPaymentService().makePayment
import SeatReservationService from './thirdparty/seatbooking/SeatReservationService.js'
const reserveSeats = new SeatReservationService().reserveSeats

const gateway = {
  makePayment,
  reserveSeats
}

const handlers = {
  initHealthcheckHandler
}

const helpers = {
  initStartServer
}

import routing from './routes/index.js'

const startServer = initServer({
  app,
  C,
  exit,
  gateway,
  handlers,
  helpers,
  logger,
  routing
})

startServer()
