module.exports = ({
  C,
  logger,
  makePayment,
  reserveSeats
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
      reserveSeats(accountId, ticketsRequested.numberOfSeats)
      return true
    } catch (error) {
      logger.error(error)
    }
  }

  return {
    purchaseTickets
  }
}
