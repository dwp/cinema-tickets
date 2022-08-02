import express from 'express'

import C from './constants/index.js'
import routing from './routes/index.js'

// init functions
import initServer from './server.js'
import initHealthcheckHandler from './handlers/healthcheck.js'
import initTicketHandler from './handlers/tickets.js'
import initStartServer from './helpers/startServer.js'
import initTicketService from './services/ticketService.js'

// third party provided 'services'
import TicketPaymentService from './thirdparty/paymentgateway/TicketPaymentService.js'
import SeatReservationService from './thirdparty/seatbooking/SeatReservationService.js'
const makePayment = new TicketPaymentService().makePayment
const reserveSeats = new SeatReservationService().reserveSeats

// config
const logger = console
const exit = process.exit
const app = express()

// objects to pass to server config
const gateway = {
  makePayment,
  reserveSeats
}

const handlers = {
  initHealthcheckHandler,
  initTicketHandler
}

const helpers = {
  initStartServer
}

const services = {
  initTicketService
}

const startServer = initServer({
  app,
  C,
  exit,
  gateway,
  handlers,
  helpers,
  logger,
  routing,
  services
})

startServer()
