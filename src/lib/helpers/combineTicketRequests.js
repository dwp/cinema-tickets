export function combineTicketRequests (ticketRequests) {
    const combinedRequest = {
        ADULT: 0,
        CHILD: 0,
        INFANT: 0,
    }

    if(!ticketRequests) {
        throw new Error("Error in combineTicketRequests: no requests were provided!")
    }

    for(const request of ticketRequests) {
        combinedRequest[`${request.getTicketType()}`] += request.getNoOfTickets();
    }

    return combinedRequest;
}