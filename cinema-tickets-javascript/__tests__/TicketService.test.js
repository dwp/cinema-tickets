import InvalidPurchaseException from "../src/pairtest/lib/InvalidPurchaseException";
import TicketTypeRequest from "../src/pairtest/lib/TicketTypeRequest";
import TicketService from "../src/pairtest/TicketService";

/* Rules
- DONE - All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets.
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

  test("purchaseTickets request of a invalid id of 0 return an InevalidPurchaseException error", () => {
    try {
      const customer = new TicketService();
      customer.purchaseTickets(0, { ADULT: 4, INFANT: 4, CHILD: 4 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("accountIdError");
      expect(error.message).toBe("accounId must be greater than 0.");
    }
  });

  test("purchaseTickets request of a invalid id of -1 return an InevalidPurchaseException error", () => {
    try {
      const customer = new TicketService();
      customer.purchaseTickets(-1, { ADULT: 4, INFANT: 4, CHILD: 4 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("accountIdError");
      expect(error.message).toBe("accounId must be greater than 0.");
    }
  });
});

describe("TicketTypeRequest", () => {
  //valid
  //no tickets
  //infants only
  //infant exceeds adult
  //more than 20
  describe("TicketTypeRequestObject returns correct value for ticket types", () => {
    test("returns correct property values for ADULT tickets", () => {
        const customer = new TicketTypeRequest("ADULT", 5);
        expect(customer.getTicketType()).toBe("ADULT");
        expect(customer.getNoOfTickets()).toBe(5);
    });
    test("returns correct property values for CHILD tickets", () => {
        const customer = new TicketTypeRequest("CHILD", 3);
        expect(customer.getTicketType()).toBe("CHILD");
        expect(customer.getNoOfTickets()).toBe(3);
    });
    test("returns correct property values for INFANT tickets", () => {
        const customer = new TicketTypeRequest("INFANT", 0);
        expect(customer.getTicketType()).toBe("INFANT");
        expect(customer.getNoOfTickets()).toBe(0);
    });
  });
});
//Calculates the correct no of seats to reserve and makes a seat reservation request to the SeatReservationService.
