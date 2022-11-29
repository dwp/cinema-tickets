import { TICKET_PRICE_LIST } from '../../constants.js'

export default function calculatePayment (ticketRequest) {
  let total = 0;
  // infants go free, don't include them in calculation
  ['ADULT', 'CHILD'].forEach((ticketType) => {
    total += (ticketRequest[ticketType] * TICKET_PRICE_LIST[ticketType])
  })
  return total
}
