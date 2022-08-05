export default ({
  C,
  logger,
  helpers,
  services
}) => (req, res) => {
  logger.log(`Request to ${C.routes.tickets.path}`)
  try {
    // throws error if validation fails
    helpers.validateRequest({
      accountId: req.body.accountId,
      ticketRequest: req.body.ticketsRequested
    })
    const numberOfSeatsRequested = helpers.calculateSeatsToReserve({ tickets: req.body.ticketsRequested })
    const totalPayment = helpers.calculateTotalPayment({ tickets: req.body.ticketsRequested })
    const ticketsRequested = {
      numberOfSeatsRequested,
      totalPayment
    }
    const result = services
      .ticketService
      .purchaseTickets({
        accountId: req.body.accountId,
        ticketsRequested
      })

    res
      .status(C.serverConfig.responseCodes.success)
      .send({
        success: result,
        numberOfSeatsRequested,
        totalPayment
      })
  } catch (error) {
    const errors = error
      .toString()
      .replace(/Error:\s/, '')
      .split(',') // quick and dirty, means errors can't have commas
    logger.error('Returning error to client')
    res
      .status(C.serverConfig.responseCodes.error)
      .send({
        success: false,
        statusCode: C.serverConfig.responseCodes.error,
        errors
      })
  }
}
