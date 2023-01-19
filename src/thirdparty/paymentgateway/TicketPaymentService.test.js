import TicketPaymentService from "./TicketPaymentService.js";
describe("TicketPaymentService", () => {
    const tPS = new TicketPaymentService();

    test("should exist", () => {
        expect(TicketPaymentService).toBeDefined();
    });

    describe("failure scenarios", () => {
        test("should throw error if accountId not integer", () => {
            expect(() => {
                tPS.makePayment("898", 40)
            }).toThrow(new TypeError("accountId must be an integer"));
        });
    
        test("should throw error if accountId is undefined", () => {
            expect(() => {
                tPS.makePayment(undefined, 40)
            }).toThrow(new TypeError("accountId must be an integer"));
        });
    
        test("should throw error if accountId is null", () => {
            expect(() => {
                tPS.makePayment(null, 40)
            }).toThrow(new TypeError("accountId must be an integer"));
        });
    
        test("should throw error if amount to pay not integer", () => {
            expect(() => {
                tPS.makePayment(898, "FORTY QUID")
            }).toThrow(new TypeError("totalAmountToPay must be an integer"));
        });
    
        // The external service does not currently return an error if a zero or negative payment is requested
        // Exposing potential issue with TicketPaymentService code as it will allow a request to be created with 0 or negative integer
        
        // In reality I would add these tests without 'not' which would fail 
        // and report the issue via Gitlab or other means to the relevant team

        // currently testing the behaviour as it is presented
        test("should not throw error if amount to pay is zero", () => {
            expect(() => {
                tPS.makePayment(898, 0)
            }).not.toThrow(new TypeError("totalAmountToPay must be an integer"));
        });
    
        test("should not throw error if amount to pay is negative", () => {
            expect(() => {
                tPS.makePayment(898, -1)
            }).not.toThrow(new TypeError("totalAmountToPay must be an integer"));
        });
    
        // The external service does not currently return an error if accountId is 0 or negative integer
        // However this may be our own internal requirement, other users may allow these values
        // I would report this as potential issue via Git repo or contact the dev team

        // currently testing the behaviour as it is presented
        test("should not throw error if accountId is zero", () => {
            expect(() => {
                tPS.makePayment(0, 2);
            }).not.toThrow(new TypeError("accountId must be an integer"));
        });
    
        test("should not throw error if accountId is negative integer", () => {
            expect(() => {
                tPS.makePayment(-1, 2);
            }).not.toThrow(new TypeError("accountId must be an integer"));
        }); 
    });

    describe("success scenarios", () => {
        test("should not error if a correctly formatted request is made", () => {
            expect(() => {
                tPS.makePayment(898,40)
            }).not.toThrow()
        })
    }) 
})
