import { describe, it, expect, jest, beforeEach } from "@jest/globals";

const mockMakePayment = jest.fn();
const mockReserveSeat = jest.fn();

jest.unstable_mockModule('../src/thirdparty/paymentgateway/TicketPaymentService.js', () => ({
    default: jest.fn().mockImplementation(() => ({
        makePayment: mockMakePayment
    }))
}));

jest.unstable_mockModule('../src/thirdparty/seatbooking/SeatReservationService.js', () => ({
    default: jest.fn().mockImplementation(() => ({
        reserveSeat: mockReserveSeat
    }))
}));

const { default: TicketService } = await import('../src/pairtest/TicketService.js');
const { default: TicketTypeRequest } = await import('../src/pairtest/lib/TicketTypeRequest.js');
const { default: InvalidPurchaseException } = await import('../src/pairtest/lib/InvalidPurchaseException.js');

describe("purchaseTickets", () => {
    beforeEach(() => {
        mockMakePayment.mockClear();
        mockReserveSeat.mockClear();
    });

    it("should pay £40 for 1 Adult, 1 Child and 1 Infant ticket", () => {
        const ticketService = new TicketService();
        ticketService.purchaseTickets(
            1,
            new TicketTypeRequest("ADULT", 1),
            new TicketTypeRequest("CHILD", 1),
            new TicketTypeRequest("INFANT", 1)
        );
        expect(mockMakePayment).toHaveBeenCalledWith(1, 40);
    });

    it("should pay £25 and reserve 1 seat for 1 Adult", () => {
        const ticketService = new TicketService();
        ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 1));
        expect(mockMakePayment).toHaveBeenCalledWith(1, 25);
        expect(mockReserveSeat).toHaveBeenCalledWith(1, 1);
    })

    it("should reserve 2 seats for 1 Adult, 1 Child and 1 Infant (infant gets no seat)", () => {
        const ticketService = new TicketService();
        ticketService.purchaseTickets(
            1,
            new TicketTypeRequest("ADULT", 1),
            new TicketTypeRequest("CHILD", 1),
            new TicketTypeRequest("INFANT", 1)
        );
        expect(mockReserveSeat).toHaveBeenCalledWith(1, 2);
    });

    it("should throw when account ID is not an integer", () => {
        const ticketService = new TicketService();
        expect(() => ticketService.purchaseTickets('this is not valid', new TicketTypeRequest("ADULT", 1))).toThrow(InvalidPurchaseException);
    })

    it("should throw TypeError when an invalid ticket type is provided", () => {
        expect(() => new TicketTypeRequest("SENIOR", 1)).toThrow(TypeError);
    });

    it('should throw when a non-TicketTypeRequest object is included in the request', () => {
        const ticketService = new TicketService();
        expect(() => ticketService.purchaseTickets(1, new TicketTypeRequest("ADULT", 1), {})).toThrow(InvalidPurchaseException);
    });

    it("should throw when acccount ID is a decimal", () => {
        const ticketService = new TicketService();
        expect(() => ticketService.purchaseTickets(1.5, new TicketTypeRequest("ADULT", 1))).toThrow(InvalidPurchaseException);
    })

    it('should throw InvalidPurchaseException when more than 25 tickets are purchased', () => {
        const ticketService = new TicketService();
        expect(() =>
            ticketService.purchaseTickets(1, new TicketTypeRequest('ADULT', 26))
        ).toThrow(InvalidPurchaseException);
    });

    it("should throw when account ID is 0", () => {
        const ticketService = new TicketService();
        expect(() => ticketService.purchaseTickets(0, new TicketTypeRequest("ADULT", 1))).toThrow(InvalidPurchaseException);
    });

    it("should throw when account ID is negative", () => {
        const ticketService = new TicketService();
        expect(() => ticketService.purchaseTickets(-1, new TicketTypeRequest("ADULT", 1))).toThrow(InvalidPurchaseException);
    });

    it("should throw when no tickets are provided", () => {
        const ticketService = new TicketService();
        expect(() => ticketService.purchaseTickets(1)).toThrow(InvalidPurchaseException);
    });

    it("should throw when only Child tickets are purchased", () => {
        const ticketService = new TicketService()
        expect(() => ticketService.purchaseTickets(1, new TicketTypeRequest("CHILD", 1))).toThrow(InvalidPurchaseException);
    });

    it("should throw when only Infant tickets are purchased", () => {
        const ticketService = new TicketService()
        expect(() => ticketService.purchaseTickets(1, new TicketTypeRequest("INFANT", 1))).toThrow(InvalidPurchaseException);
    });

    it("should throw when CHILD and INFANT tickets are purchased without an ADULT ticket", () => {
        const ticketService = new TicketService()
        expect(() => ticketService.purchaseTickets(1, new TicketTypeRequest("CHILD", 1), new TicketTypeRequest("INFANT", 1))).toThrow(InvalidPurchaseException);
    });

});
