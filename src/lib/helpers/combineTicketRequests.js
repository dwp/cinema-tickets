export function combineTicketRequests (ticketRequests) {
  const combinedRequest = {
    ADULT: 0,
    CHILD: 0,
    INFANT: 0
  }

  if (!ticketRequests || Object.keys(ticketRequests).length < 1) {
    throw new Error('Error in combineTicketRequests: no valid requests were provided!')
  }

  for (const request of ticketRequests) {
    combinedRequest[`${request.getTicketType()}`] += request.getNoOfTickets()
  }

  return combinedRequest
}
