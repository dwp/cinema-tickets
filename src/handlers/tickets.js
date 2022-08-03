export default ({
  C,
  logger,
  helpers,
  services
}) => async (req, res) => {
  logger.log(`Request to ${C.routes.tickets.path}`)
  logger.log('req', req.body)
  try {
    helpers.validateRequest({
      accountId: req.body.accountId,
      ticketRequest: req.body.ticketsRequested
    })
    const totalPayment = helpers.calculateTotalPayment({ tickets: req.body.ticketsRequested })
    const result = services
      .ticketService
      .purchaseTickets({ accountId: 1, ticketsRequested: {} })
    logger.log(result)
    res
      .status(C.serverConfig.responseCodes.success)
      .send(`Ticket purchase successful. X seats reserved. Total payment received: £${totalPayment}.00.`)
  } catch (error) {
    logger.error(error)
    res
      .status(C.serverConfig.responseCodes.error)
      .send('Internal application error')
  }
}
