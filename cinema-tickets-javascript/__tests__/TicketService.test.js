import InvalidPurchaseException from "../src/pairtest/lib/InvalidPurchaseException";
import TicketTypeRequest from "../src/pairtest/lib/TicketTypeRequest";
import TicketService from "../src/pairtest/TicketService";

describe("TicketTypeRequest Object", () => {
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

describe("TicketService.purchaseTickets() -> #checkId", () => {
  test("purchaseTickets request of a valid accountId greater than 0 does not return an error", () => {
    const customer = new TicketService();
    const test = () => {
      customer.purchaseTickets(1, { ADULT: 4, INFANT: 4, CHILD: 4 });
    };
    expect(test).not.toThrow(Error);
  });

  test("purchaseTickets request of a invalid accountId of 0 return an InevalidPurchaseException error", () => {
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

  test("purchaseTickets request of a invalid accountId of -1 return an InevalidPurchaseException error", () => {
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

// see "README.md "Notes for the examiner from the candidate" 2. for reasoning.
// see "README.md "Notes for the examiner from the candidate" 4. for reasoning.
describe("TicketService.purchaseTickets() -> SeatReservationService and -> TicketPaymentService", () => {
  test("SeatReservationService returns correct booking object for seat reservations and ticket payments of ADULTS only", () => {
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
        totalTicketsCost: 20,
      });
    };
    expect(test).not.toThrow(Error);
  });
  test("SeatReservationService returns correct booking object for seat reservations and ticket payments of ADULTS and CHILDREN only", () => {
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
        totalTicketsCost: 50,
      });
    };
    expect(test).not.toThrow(Error);
  });
  test("SeatReservationService returns correct booking object for seat reservations and ticket payments of ADULTS and INFANT only", () => {
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
        totalTicketsCost: 100,
      });
    };
    expect(test).not.toThrow(Error);
  });
  test("SeatReservationService returns correct booking object for seat reservations and ticket payments of ADULT, INFANT and CHILD", () => {
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
        totalTicketsCost: 120,
      });
    };
    expect(test).not.toThrow(Error);
  });
});



