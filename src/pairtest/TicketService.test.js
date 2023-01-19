import HelperService from "./utils/helper/HelperService.js";
import InvalidPurchaseException from "./lib/InvalidPurchaseException.js";
import TicketService from "./TicketService.js";
import SeatReservationService from "../thirdparty/seatbooking/SeatReservationService.js";
import TicketPaymentService from "../thirdparty/paymentgateway/TicketPaymentService.js";
import * as testdata from "../../test/testdata.js";

// required import to enable full usage of jest mocks against ES6 modules
import { jest}  from "@jest/globals"

describe("TicketService", () => {
    
    let myMockSRS, myMockTPS;
    const srs = new SeatReservationService();
    const tps = new TicketPaymentService();
    const help = new HelperService();

    const myTicketService =  new TicketService(srs,tps,help);

    beforeEach(() => {
        // reset any previous mock and mock the appropriate services anew
        jest.resetAllMocks();
        myMockTPS = jest.spyOn(TicketPaymentService.prototype, "makePayment");
        myMockSRS = jest.spyOn(SeatReservationService.prototype, "reserveSeat");
    })
    test("should take payment, book seat and return succesful if an adult request present", () => {
        const result = myTicketService.purchaseTickets(testdata.goodAccountNum,[testdata.requestChild, testdata.requestInfant, testdata.requestAdult]);  
        expect(myMockTPS).toHaveBeenCalled();
        expect(myMockSRS).toHaveBeenCalled();
        expect(result).toEqual("Booking successful");        
    })

    test("should take payment, book seat and return succesful for exactly twenty ticket requests", () => {
        const result = myTicketService.purchaseTickets(testdata.goodAccountNum,[testdata.requestTwenty]);
        expect(myMockTPS).toHaveBeenCalled();
        expect(myMockSRS).toHaveBeenCalled();
        expect(result).toEqual("Booking successful");
    })

    test("should throw error if no adult request present", () => {
        expect(() => {
            myTicketService.purchaseTickets(testdata.goodAccountNum, [testdata.requestChild]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: An adult must be present"));
        expect(myMockTPS).not.toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled();        
    });

    test("should throw error if insufficient adult requests present", () => {
        expect(() => {
            myTicketService.purchaseTickets(testdata.goodAccountNum, [testdata.requestAdult, testdata.requestTooManyInfants]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: All infants must sit on an adult lap"));
        expect(myMockTPS).not.toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled();        
    });

    test("should throw error if too many seats requested", () => {
        expect(() => {
            myTicketService.purchaseTickets(testdata.goodAccountNum, [testdata.requestTwenty, testdata.requestInfant]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: Ticket booking limit is 20"));
        expect(myMockTPS).not.toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled(); 
                
    });

    test("should throw error if account number is not an integer", () => {
        expect(() => {
            myTicketService.purchaseTickets(testdata.badAccountNum, [testdata.requestAdult]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: Invalid account ID provided"));
        expect(myMockTPS).not.toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled();        
    });

    test("should throw error if account number is zero", () => {
        expect(() => {
            myTicketService.purchaseTickets(0, [testdata.requestAdult]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: Invalid account ID provided"));
        expect(myMockTPS).not.toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled();         
    });

    test("should throw error if account number is below zero", () => {
        expect(() => {
            myTicketService.purchaseTickets(-1, [testdata.requestAdult]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: Invalid account ID provided"));
        expect(myMockTPS).not.toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled();          
    });

    test("with random failure in external seat reservation service it should handle and propagate error", () => {

        myMockSRS.mockImplementation(() => {
            throw new Error("User not found");
        });

        expect(() => {
            myTicketService.purchaseTickets(testdata.goodAccountNum, [testdata.requestAdult]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: seat booking failure: Error: User not found"));
        expect(myMockTPS).toHaveBeenCalled();
        expect(myMockSRS).toHaveBeenCalled();   
    });

    test("with random failure in external ticket payment service it should handle and propagate error and not reserve seats", () => {
        myMockTPS.mockImplementation(() => {
            throw new Error("User not found");
          });

        expect(() => {
            myTicketService.purchaseTickets(testdata.goodAccountNum, [testdata.requestAdult]);
        }).toThrow(new InvalidPurchaseException("Error during booking: Error: payment failure: Error: User not found"));
        expect(myMockTPS).toHaveBeenCalled();
        expect(myMockSRS).not.toHaveBeenCalled(); 
    });

    test("with failure in internal service it should throw error", () => {
        const myFakeTicketService = jest.spyOn(TicketService.prototype, "purchaseTickets");
        myFakeTicketService.mockImplementation(() => {
            throw new Error("Fake internal error");
        });
        
        expect(() => {
            myTicketService.purchaseTickets(testdata.goodAccountNum, [testdata.requestAdult]);
        }).toThrow(new InvalidPurchaseException("Fake internal error"));
    });
})
