module.exports = ({
  C
}) => async (req, res) => {
  console.log(`Request to ${C.routes.healthcheck.path}`)
  try {
    res
      .status(C.serverConfig.responseCodes.success)
      .send(C.routes.healthcheck.responseString)
  } catch (error) {
    console.error(error)
    res
      .status(C.serverConfig.responseCodes.error)
      .send('Internal application error')
  }
}
