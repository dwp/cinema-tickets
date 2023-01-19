import SeatReservationService from "./SeatReservationService.js";
describe("SeatReservationService", () => {
    const sRS = new SeatReservationService();
    test("should exist", () => {
        expect(SeatReservationService).toBeDefined();
    });

    test("should throw error if accountId not integer", () => {
        expect(() => {
            sRS.reserveSeat("898", 2);
        }).toThrow(new TypeError("accountId must be an integer"));
    })

    test("should throw error if no of seats not integer", () => {
        expect(() => {
            sRS.reserveSeat(898, "FOUR");
        }).toThrow(new TypeError("totalSeatsToAllocate must be an integer"));
    });

    // The external service does not currently return an error if a zero or negative accountId is present
    // However this may be our own internal requirement, other users may allow these values
    // I would report this as potential issue via Git repo or contact the dev team
    test("should not throw error if accountId is zero", () => {
        expect(() => {
            sRS.reserveSeat(0, 2);
        }).not.toThrow(new TypeError("accountId must be an integer"));
    });

    test("should not throw error if accountId is negative integer", () => {
        expect(() => {
            sRS.reserveSeat(-1, 2);
        }).not.toThrow(new TypeError("accountId must be an integer"));
    });

    test("should not throw error for a correctly formatted reservation request", () => {
        expect(() => {
            sRS.reserveSeat(123, 3)
        }).not.toThrow()
    })

    // The external service does not currently return an error if a zero or negative seat reservation is requested
    // Exposing potential issue with SeatReservationService code as it will allow a request to be created with 0 or negative integer
    // In reality I would add these tests without 'not' which would fail 
    // and report the issue via Gitlab or other means to the SeatReservationService dev

    // currently testing the behaviour as it is presented
    test("should not throw error if no of seats is zero", () => {
        expect(() => {
            sRS.reserveSeat(898, 0);
        }).not.toThrow(new TypeError("totalSeatsToAllocate must be an integer"));
    });
    test("should not throw error if no of seats is negative", () => {
        expect(() => {
            sRS.reserveSeat(898, -1);
        }).not.toThrow(new TypeError("totalSeatsToAllocate must be an integer"));
    });
});
