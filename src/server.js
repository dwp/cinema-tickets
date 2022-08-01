module.exports = ({
  app,
  C,
  exit,
  handlers,
  helpers,
  logger,
  routing
}) => {
  // return initialised startServer function
  const startServer = helpers.initStartServer({
    app,
    exit: exit,
    host: C.serverConfig.host,
    logger,
    port: C.serverConfig.port,
    startMsg: C.serverConfig.startMsg
  })

  const initialisedHandlers = {
    healthcheck: handlers.initHealthcheckHandler({ C })
  }

  routing({
    app,
    C,
    handlers: initialisedHandlers
  })

  return startServer
}
