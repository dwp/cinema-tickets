export default ({
  app,
  C,
  exit,
  gateway,
  handlers,
  helpers,
  logger,
  routing,
  services
}) => {
  // return initialised startServer function
  const startServer = helpers.initStartServer({
    app,
    exit,
    host: C.serverConfig.host,
    logger,
    port: C.serverConfig.port,
    startMsg: C.serverConfig.startMsg
  })

  const initialisedServices = {
    ticketService: services.initTicketService({
      C,
      logger,
      makePayment: gateway.makePayment,
      reserveSeats: gateway.reserveSeats
    })
  }

  const initialisedHandlers = {
    healthcheck: handlers.initHealthcheckHandler({ C, logger }),
    tickets: handlers.initTicketHandler({ C, logger, services: initialisedServices })
  }

  routing({
    app,
    C,
    handlers: initialisedHandlers
  })

  return startServer
}
