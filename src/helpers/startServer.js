export default ({
  app,
  exit,
  host,
  logger,
  port,
  startMsg
}) => () => {
  try {
    app.listen(port, host)
    logger.log(`${startMsg} ${host}:${port}`)
  } catch (error) {
    logger.error(error)
    exit(1)
  }
}
