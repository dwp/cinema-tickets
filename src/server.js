module.exports = ({
  app,
  C,
  handlers,
  helpers,
  routing
}) => {
  // return initialised startServer function
  const startServer = helpers.initStartServer({
    app,
    exit: process.exit,
    host: C.serverConfig.host,
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
