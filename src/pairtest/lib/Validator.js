class Validator {
  static isTicketCountValid(...ticketTypeRequests) {
    const totalTickets = ticketTypeRequests.reduce((total, request) => total + request.count, 0);
    return totalTickets <= 20;
  }

  static hasAccompanyingAdult(...ticketTypeRequests) {
    let adultCount = 0;
    for (const request of ticketTypeRequests) {
      if (request.type === 'ADULT') {
        adultCount += request.count;
      }
    }
    return adultCount > 0;
  }
}

export default Validator;
