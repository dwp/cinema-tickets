export default ({
  C,
  logger
}) => ({ req, res }) => {
  logger.log(`Request to ${C.routes.healthcheck.path}`)
  try {
    res
      .status(C.serverConfig.responseCodes.success)
      .send(C.routes.healthcheck.responseString)
  } catch (error) {
    logger.error(error)
    res
      .status(C.serverConfig.responseCodes.error)
      .send('Internal application error')
  }
}
