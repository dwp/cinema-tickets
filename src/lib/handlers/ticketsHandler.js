import TicketTypeRequest from "../TicketTypeRequest.js"
import TicketService from "../services/TicketService.js"


export function ticketsHandler (ticketsRequestBody) {
    const { accountId, ticketRequests } = ticketsRequestBody;
    if(!ticketsRequestBody.accountId) {
        throw new Error("No Account ID provided")
    }

    if(!ticketsRequestBody.ticketRequests) {
        throw new Error("No Ticket Requests provided")
    }

    let transformedTicketRequests = []
    for(let request of ticketsRequestBody.ticketRequests) {
        transformedTicketRequests.push(
            new TicketTypeRequest(request.type, request.noOfTickets)
        )
    }

    const ticketService = new TicketService();
    return ticketService.purchaseTickets(accountId, ...transformedTicketRequests)
}