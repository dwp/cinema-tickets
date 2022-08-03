export default ({
  C,
  logger,
  helpers,
  services
}) => async (req, res) => {
  logger.log(`Request to ${C.routes.tickets.path}`)
  logger.log('req', req.body)
  try {
    const result = services
      .ticketService
      .purchaseTickets({ accountId: 1, ticketsRequested: {} })
    logger.log(result)
    res
      .status(C.serverConfig.responseCodes.success)
      .send('Ticket purchase successful')
  } catch (error) {
    logger.error(error)
    res
      .status(C.serverConfig.responseCodes.error)
      .send('Internal application error')
  }
}
