import { describe, it, expect, jest } from "@jest/globals";
import TicketService from "../src/pairtest/TicketService.js";
import TicketTypeRequest from "../src/pairtest/lib/TicketTypeRequest.js";
import InvalidPurchaseException from "../src/pairtest/lib/InvalidPurchaseException.js";

const createTicketService = () => {
  const paymentService = {
    makePayment: jest.fn(),
  };

  const seatReservationService = {
    reserveSeat: jest.fn(),
  };

  const ticketService = new TicketService(
    paymentService,
    seatReservationService,
  );

  return { ticketService, paymentService, seatReservationService };
};

describe("TicketService", () => {
  describe("purchaseTickets - validation", () => {
    it("throws error when account id is less than 1", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(0, new TicketTypeRequest("ADULT", 1));
      }).toThrow(InvalidPurchaseException);
    });

    it("throws error when no ticket requests are provided", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1);
      }).toThrow(InvalidPurchaseException);
    });

    it("throws error when ticket quantity is zero", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 0));
      }).toThrow(InvalidPurchaseException);
    });

    it("throws error when ticket quantity is negative", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", -1));
      }).toThrow(InvalidPurchaseException);
    });

    it("throws error when child tickets are purchased without an adult", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1, new TicketTypeRequest("CHILD", 1));
      }).toThrow(InvalidPurchaseException);
    });

    it("throws error when infant tickets are purchased without an adult", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1, new TicketTypeRequest("INFANT", 1));
      }).toThrow(InvalidPurchaseException);
    });

    it("throws error when more than 25 tickets are purchased", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 26));
      }).toThrow(InvalidPurchaseException);
    });

    it("does not throw error when 25 tickets are purchased", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 25));
      }).not.toThrow();
    });

    it("throws error when combined ticket types exceed 25", () => {
        const ticketService = new TicketService();

        expect(() => {
          ticketService.purchaseTickets(
            1,
            new TicketTypeRequest("ADULT", 10),
            new TicketTypeRequest("CHILD", 10),
            new TicketTypeRequest("INFANT", 6),
          );
        }).toThrow(InvalidPurchaseException);
      });

    it("allows exactly 25 tickets across mixed types", () => {
        const ticketService = new TicketService();

        expect(() => {
          ticketService.purchaseTickets(
            1,
            new TicketTypeRequest("ADULT", 10),
            new TicketTypeRequest("CHILD", 10),
            new TicketTypeRequest("INFANT", 5),
          );
        }).not.toThrow();
      });

    it("throws error when there are more infants than adults", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(
          1,
          new TicketTypeRequest("ADULT", 1),
          new TicketTypeRequest("INFANT", 2),
        );
      }).toThrow(InvalidPurchaseException);
    });

    it("does not throw error when infants equal adults", () => {
      const ticketService = new TicketService();

      expect(() => {
        ticketService.purchaseTickets(
          1,
          new TicketTypeRequest("ADULT", 2),
          new TicketTypeRequest("INFANT", 2),
        );
      }).not.toThrow();
    });

    it("does not call payment or seat services when purchase is invalid", () => {
      const { ticketService, paymentService, seatReservationService } =
        createTicketService();

      expect(() => {
        ticketService.purchaseTickets(
          1,
          new TicketTypeRequest("CHILD", 1), // invalid (no adult)
        );
      }).toThrow(InvalidPurchaseException);

      expect(paymentService.makePayment).not.toHaveBeenCalled();
      expect(seatReservationService.reserveSeat).not.toHaveBeenCalled();
    });
  });

  describe("purchaseTickets - payment", () => {
    it("charges £25 for 1 adult ticket", () => {
      const { ticketService, paymentService } = createTicketService();

      ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 1));

      expect(paymentService.makePayment).toHaveBeenCalledWith(1, 25);
    });
    it("charges correct total for adults and children", () => {
      const { ticketService, paymentService } = createTicketService();

      ticketService.purchaseTickets(
        1,
        new TicketTypeRequest("ADULT", 2),
        new TicketTypeRequest("CHILD", 1),
      );

      expect(paymentService.makePayment).toHaveBeenCalledWith(1, 65);
    });
    it("does not charge for infant tickets", () => {
      const { ticketService, paymentService } = createTicketService();

      ticketService.purchaseTickets(
        1,
        new TicketTypeRequest("ADULT", 2),
        new TicketTypeRequest("INFANT", 2),
      );

      // 2 adults = 50, infants = 0 total = 50
      expect(paymentService.makePayment).toHaveBeenCalledWith(1, 50);
    });
  });
  describe("purchaseTickets - seat reservation", () => {
    it("reserves 1 seat for 1 adult ticket", () => {
      const { ticketService, seatReservationService } = createTicketService();

      ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 1));

      expect(seatReservationService.reserveSeat).toHaveBeenCalledWith(1, 1);
    });

    it("reserves seats for adults and children", () => {
      const { ticketService, seatReservationService } = createTicketService();

      ticketService.purchaseTickets(
        1,
        new TicketTypeRequest("ADULT", 2),
        new TicketTypeRequest("CHILD", 3),
      );

      expect(seatReservationService.reserveSeat).toHaveBeenCalledWith(1, 5);
    });

    it("does not reserve seats for infant tickets", () => {
      const { ticketService, seatReservationService } = createTicketService();

      ticketService.purchaseTickets(
        1,
        new TicketTypeRequest("ADULT", 1),
        new TicketTypeRequest("INFANT", 1),
      );

      expect(seatReservationService.reserveSeat).toHaveBeenCalledWith(1, 1);
    });
  });
  
  describe("purchaseTickets - service interaction", () => {
    it("calls both payment and seat services with correct values", () => {
      const { ticketService, paymentService, seatReservationService } =
        createTicketService();

      ticketService.purchaseTickets(
        1,
        new TicketTypeRequest("ADULT", 2),
        new TicketTypeRequest("CHILD", 2),
        new TicketTypeRequest("INFANT", 1),
      );

      expect(paymentService.makePayment).toHaveBeenCalledWith(1, 80);

      expect(seatReservationService.reserveSeat).toHaveBeenCalledWith(1, 4);
    });
  });
});
