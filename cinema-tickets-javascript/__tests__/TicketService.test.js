import InvalidPurchaseException from "../src/pairtest/lib/InvalidPurchaseException";
import TicketTypeRequest from "../src/pairtest/lib/TicketTypeRequest";
import TicketService from "../src/pairtest/TicketService";

/* Rules
- DONE - All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets.
- DONE - Child and Infant tickets cannot be purchased without purchasing an Adult ticket.
- DONE - Only a maximum of 20 tickets that can be purchased at a time.
- DONE -  Multiple tickets can be purchased at any given time.
- DONE -Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap.
- The ticket prices are based on the type of ticket (see table below).

total number seat request (not infants) -> seat request
payment calc
payment request
*/

// Calculates the correct amount for the requested tickets and makes a payment request to the TicketPaymentService.
describe("TicketService.purchaseTickets() -> #checkId", () => {
  test("purchaseTickets request of a valid id greater than 0 does not return an error", () => {
    const customer = new TicketService();
    const test = () => {
      customer.purchaseTickets(1, { ADULT: 4, INFANT: 4, CHILD: 4 });
    };
    expect(test).not.toThrow(Error);
  });

  test("purchaseTickets request of a invalid id of 0 return an InevalidPurchaseException error", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(0, { ADULT: 4, INFANT: 4, CHILD: 4 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(0, { ADULT: 4, INFANT: 4, CHILD: 4 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("accountIdError");
      expect(error.message).toBe("accountId must be greater than 0.");
    }
  });

  test("purchaseTickets request of a invalid id of -1 return an InevalidPurchaseException error", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(-1, { ADULT: 4, INFANT: 4, CHILD: 4 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(-1, { ADULT: 4, INFANT: 4, CHILD: 4 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("accountIdError");
      expect(error.message).toBe("accountId must be greater than 0.");
    }
  });
});

describe("TicketService.purchaseTickets() -> TicketTypeRequestObject", () => {
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
  test("throws an InvalidPurchaseException error for negative numbers of tickets", () => {
    const test = () => {
      const customer = new TicketTypeRequest("ADULT", -5);
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketTypeRequest("ADULT", -5);
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("ticketNumberError");
      expect(error.message).toBe(
        "Numbers of tickets purchased must be 0 or greater."
      );
    }
  });
});

describe("TicketService.purchaseTickets() -> #checkAdultsPresentForChildrenInfants", () => {
  test("throws an InvalidPurchaseException error if CHILD tickets purchased without ADULT tickets", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 0, CHILD: 4 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 0, CHILD: 4 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("invalidNumberOfAdultTicketsError");
      expect(error.message).toBe(
        "Cannot purchase CHILD or INFANT tickets without purchasing ADULT tickets"
      );
    }
  });
  test("throws an InvalidPurchaseException error if INFANT tickets purchased without ADULT tickets", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 5, CHILD: 0 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 5, CHILD: 0 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("invalidNumberOfAdultTicketsError");
      expect(error.message).toBe(
        "Cannot purchase CHILD or INFANT tickets without purchasing ADULT tickets"
      );
    }
  });
  test("throws an InvalidPurchaseException error if INFANT AND CHILD tickets purchased without ADULT tickets", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 5, CHILD: 5 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 5, CHILD: 5 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("invalidNumberOfAdultTicketsError");
      expect(error.message).toBe(
        "Cannot purchase CHILD or INFANT tickets without purchasing ADULT tickets"
      );
    }
  });
  test("does not throw InvalidPurchaseException error if INFANT and CHILDREN present with ADULT", () => {
    const customer = new TicketService();
    const test = () => {
      customer.purchaseTickets(1, { ADULT: 1, INFANT: 1, CHILD: 1 });
    };
    expect(test).not.toThrow(InvalidPurchaseException);
  });
});

describe("TicketService.purchaseTickets() -> #countTickets", () => {
  test("does not throw InvalidPurchaseException error if total number of tickets between 0 exclusive and 20 inclusive", () => {
    let numOfTickets = 1;
    for (let i = 0; i < 20; i++) {
      const customer = new TicketService();
      const test = () => {
        customer.purchaseTickets(1, {
          ADULT: numOfTickets,
          INFANT: 0,
          CHILD: 0,
        });
      };
      expect(test).not.toThrow(InvalidPurchaseException);
      numOfTickets += 1;
    }
  });
  test("throws InvalidPurchaseException error if total number of tickets = 0", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 0, CHILD: 0 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 0, INFANT: 0, CHILD: 0 });
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("invalidNumberOfTicketsError");
      expect(error.message).toBe("Cannot purchase 0 tickets.");
    }
  });
  test("throws InvalidPurchaseException error if total number of tickets > 20", () => {
    const test = () => {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 18, INFANT: 2, CHILD: 1 });
    };
    expect(test).toThrow(InvalidPurchaseException);
    try {
      const customer = new TicketService();
      customer.purchaseTickets(1, { ADULT: 18, INFANT: 2, CHILD: 1 });
      expect(
        customer.purchaseTickets(1, { ADULT: 18, INFANT: 2, CHILD: 1 })
      ).toThrow(InvalidPurchaseException);
    } catch (error) {
      expect(error).toBeInstanceOf(InvalidPurchaseException);
      expect(error.subType).toBe("invalidNumberOfTicketsError");
      expect(error.message).toBe("Cannot purchase more than 20 tickets.");
    }
  });
});

// see "README.ms "Notes for the examiner from the candidate" 2. for reasoning.
describe("TicketService.purchaseTickets() -> SeatReservationService", () => {
  test("SeatReservationService returns correct booking object for seat reservations of ADULTS only", () => {
    const test = () => {
      const customer = new TicketService();
      const actual = customer.purchaseTickets(1, {
        ADULT: 1,
        INFANT: 0,
        CHILD: 0,
      });
      expect(actual).toEqual({
        accountId: 1,
        bookingSuccessful: true,
        totalNoOfSeatsReserved: 1,
      });
    };
    expect(test).not.toThrow(Error);
  });
  test("SeatReservationService returns correct booking object for seat reservations of ADULTS and CHILDREN only", () => {
    const test = () => {
      const customer = new TicketService();
      const actual = customer.purchaseTickets(2, {
        ADULT: 1,
        INFANT: 0,
        CHILD: 3,
      });
      expect(actual).toEqual({
        accountId: 2,
        bookingSuccessful: true,
        totalNoOfSeatsReserved: 4,
      });
    };
    expect(test).not.toThrow(Error);
  });
  test("SeatReservationService returns correct booking object for seat reservations of ADULTS and INFANT only", () => {
    const test = () => {
      const customer = new TicketService();
      const actual = customer.purchaseTickets(3, {
        ADULT: 5,
        INFANT: 3,
        CHILD: 0,
      });
      expect(actual).toEqual({
        accountId: 3,
        bookingSuccessful: true,
        totalNoOfSeatsReserved: 5,
      });
    };
    expect(test).not.toThrow(Error);
  });
  test("SeatReservationService returns correct booking object for seat reservations of ADULT,  INFANT and CHILD", () => {
    const test = () => {
      const customer = new TicketService();
      const actual = customer.purchaseTickets(4, {
        ADULT: 5,
        INFANT: 3,
        CHILD: 2,
      });
      expect(actual).toEqual({
        accountId: 4,
        bookingSuccessful: true,
        totalNoOfSeatsReserved: 7,
      });
    };
    expect(test).not.toThrow(Error);
  });
});

describe("TicketService.purchaseTickets() -> TicketPaymentService")

//Calculates the correct no of seats to reserve and makes a seat reservation request to the SeatReservationService.
