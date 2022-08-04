export default ({
  logger,
  makePayment,
  reserveSeat
}) => {
  const purchaseTickets = ({ accountId, ticketsRequested }) => {
    logger.log('Requesting ticket purchase')
    try {
      makePayment(accountId, ticketsRequested.totalPayment)
      reserveSeat(accountId, ticketsRequested.numberOfSeatsRequested)
      return true
    } catch (error) {
      logger.error(error)
      throw error
    }
  }

  return {
    purchaseTickets
  }
}
