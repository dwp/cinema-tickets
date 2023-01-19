import TicketTypeRequest from "./TicketTypeRequest.js";

describe("TicketTypeRequest", () => {
    test("should exist", () => {
        expect(TicketTypeRequest).toBeDefined();
    });

    describe("failure scenarios", () => {
        test("should throw error if type does not exist", () => {
            expect(() => {
               new TicketTypeRequest("FAKE", 4);
            }).toThrow(new TypeError("type must be ADULT, CHILD, or INFANT"));
        });
    
        test("should throw error if type is undefined", () => {
            expect(() => {
               new TicketTypeRequest(undefined, 4);
            }).toThrow(new TypeError("type must be ADULT, CHILD, or INFANT"));
        });
    
        test("should throw error if type is null", () => {
            expect(() => {
               new TicketTypeRequest(null, 4);
            }).toThrow(new TypeError("type must be ADULT, CHILD, or INFANT"));
        });
    
        test("should throw error if no of tickets not integer", () => {
            expect(() => {
                new TicketTypeRequest("ADULT", "FOUR");
             }).toThrow(new TypeError("noOfTickets must be an integer"));
        });
    
        test("should throw error if no of tickets is undefined", () => {
            expect(() => {
                new TicketTypeRequest("ADULT", undefined);
             }).toThrow(new TypeError("noOfTickets must be an integer"));
        });
    
        test("should throw error if no of tickets is null", () => {
            expect(() => {
                new TicketTypeRequest("ADULT", null);
             }).toThrow(new TypeError("noOfTickets must be an integer"));
        });
    
        test("should throw error if no of tickets is zero", () => {
            expect(() => {
                new TicketTypeRequest("ADULT", 0);
             }).toThrow(new TypeError("noOfTickets must be a positive integer"));
        });
    
        test("should throw error if no of tickets is negative", () => {
            expect(() => {
                new TicketTypeRequest("ADULT", -1);
             }).toThrow(new TypeError("noOfTickets must be a positive integer"));
        });
    })
    
    describe("success scenarios", () => {
        const ticketRequest = new TicketTypeRequest("ADULT", 1);
        test("should return the number of tickets in a request", () => { 
            const numOfTickets = ticketRequest.getNoOfTickets();

            expect (numOfTickets).toBe(1);
        })

        test("should return the type of tickets in a request", () => {
            const ticketType = ticketRequest.getTicketType();

            expect (ticketType).toBe("ADULT");
        })
    })
})
