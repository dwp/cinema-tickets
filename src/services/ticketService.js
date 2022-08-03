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
      makePayment(accountId, ticketsRequested.totalPayment)
      reserveSeat(accountId, ticketsRequested.numberOfSeats)
      return true
    } catch (error) {
      logger.error(error)
      throw new Error(error)
    }
  }

  return {
    purchaseTickets
  }
}
