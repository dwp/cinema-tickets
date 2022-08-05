import express from 'express'

import C from './constants/index.js'
import routing from './routes/index.js'

// init functions
import initApp from './app.js'
import initHealthcheckHandler from './handlers/healthcheck.js'
import initTicketHandler from './handlers/tickets.js'
import initCalculateSeatsToReserve from './helpers/calculateSeatsToReserve.js'
import initCalculateTotalPayment from './helpers/calculateTotalPayment.js'
import initStartServer from './helpers/startServer.js'
import initValidateRequest from './helpers/validateRequest.js'
import initTicketService from './services/ticketService.js'

// third party provided 'services'
import TicketPaymentService from './thirdparty/paymentgateway/TicketPaymentService.js'
import SeatReservationService from './thirdparty/seatbooking/SeatReservationService.js'
const makePayment = new TicketPaymentService().makePayment
const reserveSeat = new SeatReservationService().reserveSeat

// config
const logger = console
const exit = process.exit
const app = express()

// objects to pass to server config
const gateway = {
  makePayment,
  reserveSeat
}

const handlers = {
  initHealthcheckHandler,
  initTicketHandler
}

const helpers = {
  initCalculateSeatsToReserve,
  initCalculateTotalPayment,
  initStartServer,
  initValidateRequest
}

const services = {
  initTicketService
}

const startServer = initApp({
  app,
  C,
  exit,
  express,
  gateway,
  handlers,
  helpers,
  logger,
  routing,
  services
})

startServer()
