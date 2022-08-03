export default ({
  app,
  C,
  exit,
  gateway,
  handlers,
  helpers,
  express,
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

  // initialised helpers passed to handlers/services
  const initialisedHelpers = {
    calculateSeatsToReserve: helpers.initCalculateSeatsToReserve({ C }),
    calculateTotalPayment: helpers.initCalculateTotalPayment({ C }),
    validateRequest: helpers.initValidateRequest({ C })
  }

  const initialisedServices = {
    ticketService: services.initTicketService({
      C,
      logger,
      makePayment: gateway.makePayment,
      reserveSeat: gateway.reserveSeat
    })
  }

  const initialisedHandlers = {
    healthcheck: handlers.initHealthcheckHandler({ C, logger }),
    tickets: handlers.initTicketHandler({
      C,
      express,
      helpers: initialisedHelpers,
      logger,
      services: initialisedServices
    })
  }

  routing({
    app,
    C,
    express,
    handlers: initialisedHandlers
  })

  return startServer
}
