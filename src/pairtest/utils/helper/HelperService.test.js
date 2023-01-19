import * as testdata from "../../../../test/testdata.js";
import * as services from "../../../../test/service-objects.js";


describe("HelperService", () => {
    const HELPER = services.HELPER;

    test("should exist", () => {
        expect(HELPER).toBeDefined();
    });

    describe("isAccountIDValid", () => {
        test("should contain a method to test if adult present", () => {
            expect(HELPER.isAccountIDValid).toBeDefined();
        });

        test("should return true if accountId is a positive integer", () => {
            const result = HELPER.isAccountIDValid(123);
            expect(result).toBe(true);
        }); 

        test("should return false if accountId is zero", () => {
            const result = HELPER.isAccountIDValid(0);
            expect(result).toBe(false);
        });
        
        test("should return false if accountId is negative", () => {
            const result = HELPER.isAccountIDValid(-123);
            expect(result).toBe(false);
        });
    });

    describe("isAdultPresent", () => {
        test("should contain a method to test if adult present", () => {
            expect(HELPER.isAdultPresent).toBeDefined();
        });

        test("should return true if adult is present", () => {
            const result = HELPER.isAdultPresent([testdata.requestAdult, testdata.requestChild, testdata.requestInfant]);
            expect(result).toBe(true);
        });

        test("should return false if no adult is present", () => {
            const result = HELPER.isAdultPresent([testdata.requestChild, testdata.requestInfant]);
            expect(result).toBe(false);
        }); 

    });

    describe("areEnoughAdultsPresent", () => {
        test("should contain a method to test if sufficient adults are present", () => {
            expect(HELPER.areEnoughAdultsPresent).toBeDefined();
        });

        test("should return true if enough adults are present", () => {
            const result = HELPER.areEnoughAdultsPresent([testdata.requestAdult, testdata.requestChild, testdata.requestInfant]);
            expect(result).toBe(true);
        });

        test("should return false if not enough adults are present", () => {
            const result = HELPER.areEnoughAdultsPresent([testdata.requestAdult, testdata.requestChild, testdata.requestTooManyInfants]);
            expect(result).toBe(false);
        });  
   })
  
    describe("countTicketsInRequest", () => {
        test("should contain a method to count tickets in request", () => {
            expect(HELPER.countTicketsInRequest).toBeDefined();
        });
        
        test("should count all types of ticket within ticket count", () => {
            const result = HELPER.countTicketsInRequest([testdata.requestAdult, testdata.requestChild,testdata.requestInfant]);
            expect(result).toEqual(5);
        });
    });

    describe("countSeatsInRequest", () => {
        test("should contain a method to count seats in request", () => {
            expect(HELPER.countSeatsInRequest).toBeDefined();
        });
        
        test("should count only adult and child tickets in seat count", () => {
            const result = HELPER.countSeatsInRequest([testdata.requestAdult, testdata.requestChild,testdata.requestInfant]);
            expect(result).toEqual(4);
        });
    });
    
    describe("calculatePayment", () => {
        test("should contain a method to calculate payment for the request", () => {
            expect(HELPER.calculatePayment).toBeDefined();
        });

        test("should correctly calculate the cost of adult ticket request", () => {
            const result = HELPER.calculatePayment([testdata.requestAdult]);
            expect(result).toEqual(20);
        });

        test("should correctly calculate the cost of child ticket request", () => {
            const result = HELPER.calculatePayment([testdata.requestChild]);
            expect(result).toEqual(30); //3 children on this request
        });

        test("should correctly calculate the cost of infant ticket request", () => {
            const result = HELPER.calculatePayment([testdata.requestInfant]);
            expect(result).toEqual(0); //infant goes free
        });

        test("should correctly calculate the cost of a collection of ticket requests", () => {
            const result = HELPER.calculatePayment([testdata.requestAdult, testdata.requestChild, testdata.requestInfant]);
            expect(result).toEqual(50);
        });
    });
    
})
