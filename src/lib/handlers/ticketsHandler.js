import TicketTypeRequest from '../TicketTypeRequest.js'
import TicketService from '../services/TicketService.js'
import { HttpError } from '../errors/HttpError.js'

export function ticketsHandler (ticketsRequestBody) {
  const { accountId, ticketRequests } = ticketsRequestBody
  if (!ticketsRequestBody.accountId) {
    throw new HttpError('No Account ID provided', 400)
  }

  if (!ticketRequests) {
    throw new HttpError('No Ticket Requests provided', 400)
  }

  const transformedTicketRequests = []
  for (const request of ticketRequests) {
    transformedTicketRequests.push(
      new TicketTypeRequest(request.type, request.noOfTickets)
    )
  }

  const ticketService = new TicketService()
  return ticketService.purchaseTickets(accountId, ...transformedTicketRequests)
}