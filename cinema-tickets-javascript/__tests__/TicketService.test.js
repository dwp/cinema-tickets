import TicketService from "../src/pairtest/TicketService";

/* Rules
All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets.
- There are 3 types of tickets i.e. Infant, Child, and Adult.
- The ticket prices are based on the type of ticket (see table below).
- The ticket purchaser declares how many and what type of tickets they want to buy.
- Multiple tickets can be purchased at any given time.
- Only a maximum of 20 tickets that can be purchased at a time.
- Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap.
- Child and Infant tickets cannot be purchased without purchasing an Adult ticket.

*/

// Calculates the correct amount for the requested tickets and makes a payment request to the TicketPaymentService.
describe("accountId- rejects accounts of id of 0 or less", () => {
  test("purchaseTickets request of a valid id greater than 0 does not return an error", () => {
    const customer = new TicketService();
    const test = () => {
      customer.purchaseTickets(1, { ADULT: 4, INFANT: 4, CHILD: 4 });
    };
    expect(test).not.toThrow(Error);
  });

  
});

//Calculates the correct no of seats to reserve and makes a seat reservation request to the SeatReservationService.
