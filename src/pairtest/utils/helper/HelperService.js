/**
 * External class for helper methods to allow 
 * for testability as we can create an instance 
 * of this class as a private property in TicketService
 */
 export default class HelperService {
    /**
     * Check that accountID is an integer above zero
     * @param { any } accountId 
     * @returns { Boolean } 
     */
     isAccountIDValid(accountId) {
        return Boolean(Number.isInteger(accountId) && Math.sign(accountId > 0));
    };

    /**
     * Check for the presence of adult and return Boolean
     * @param { [TicketTypeRequest] } ticketTypeRequests 
     * @returns { Boolean }
     */
    isAdultPresent(ticketTypeRequests) {
        // remember not to allow an adult request with a zero ticket count
        if (ticketTypeRequests.some(el => el.getTicketType() === "ADULT" && el.getNoOfTickets() > 0 )) {
            return true;
        };
        return false;
    };

    /**
     * Are sufficient adult laps present for the number of infants
     * @param { [TicketTypeRequest] } ticketTypeRequests 
     * @param { Integer } ticketCount 
     * @returns { Boolean }
     */
    areEnoughAdultsPresent(ticketTypeRequests, infantsRequested = 0, adultsRequested = 0) {
        ticketTypeRequests.forEach(req => {
            if (req.getTicketType() === "INFANT") infantsRequested += req.getNoOfTickets();
            if (req.getTicketType() === "ADULT") adultsRequested += req.getNoOfTickets();
        });
        if (adultsRequested < infantsRequested) {
            return false;
        };
        return true;
    };


    /**
     * Count the total number of tickets in the array of requests
     * @param { [TicketTypeRequest] } ticketTypeRequests 
     * @param { Integer } ticketCount, initialised to 0
     * @returns { Integer } count of total tickets requested
     */
    countTicketsInRequest(ticketTypeRequests, ticketCount = 0){
        ticketTypeRequests.forEach(req => {
            ticketCount += req.getNoOfTickets();
        });
        return ticketCount;
    };

    /**
     * Count the total number of seats in the array of requests
     * @param { [TicketTypeRequest] } ticketTypeRequests 
     * @param { Integer } seatCount, initialised to 0
     * @returns { Integer } count of total seats requested
     */
    countSeatsInRequest(ticketTypeRequests, seatCount = 0){
        ticketTypeRequests.forEach(req => {
            if (req.getTicketType() !== "INFANT") {
                seatCount += req.getNoOfTickets();
            };
        });
        return seatCount;
    }

    /**
     * Calculate payment due for the requests
     * @param { [TicketTypeRequest] } ticketTypeRequests 
     * @param { Integer } paymentDue, initialised to 0
     * @returns { Integer } count of payment due
     */
    calculatePayment(ticketTypeRequests, paymentDue = 0){
        ticketTypeRequests.forEach(req => {
          if (req.getTicketType() === "ADULT"){
            paymentDue += req.getNoOfTickets() * 20;
          }
          else if (req.getTicketType() === "CHILD"){
            paymentDue += req.getNoOfTickets() * 10;
          };
          // no payment due for infant so no need to loop for this
        })
        return paymentDue;
    }
}
