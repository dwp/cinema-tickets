export default ({
  C,
  logger,
  makePayment,
  reserveSeat
}) => {
  const purchaseTickets = ({ accountId, ticketsRequested }) => {
    logger.log('Requesting ticket purchase')
    try {
      /**
       * potential request format from handler
       * ticketsRequested: {
       *   numberOfSeats: number,
       *   totalPayment: number
       * }
       */
      makePayment(accountId, ticketsRequested.totalPayment = 0)
      reserveSeat(accountId, ticketsRequested.numberOfSeats = 0)
      return true
    } catch (error) {
      logger.error(error)
    }
  }

  return {
    purchaseTickets
  }
}
