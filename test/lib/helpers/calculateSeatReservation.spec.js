import { expect } from 'chai'
import calculateSeatReservation from '../../../src/lib/helpers/calculateSeatReservation.js'

describe('calculateSeatReservation', () => {
    let numberOfSeats;
    let ticketRequest
    it('should return a number', () => {
        ticketRequest = {
            ADULT: 1,
            CHILD: 0,
            INFANT: 0
        }

        numberOfSeats = calculateSeatReservation(ticketRequest)
        expect(typeof numberOfSeats === 'number').to.be.eq(true)
    });

    it('should return 1 if one adult seat is requested', () => {
        ticketRequest = {
            ADULT: 1,
            CHILD: 0,
            INFANT: 0
        }

        numberOfSeats = calculateSeatReservation(ticketRequest)

        expect(numberOfSeats).to.be.eq(1);
    })

    it('should return 2 if one child seat and one adult seat is requested', () => {
        ticketRequest = {
            ADULT: 1,
            CHILD: 1,
            INFANT: 0
        }

        numberOfSeats = calculateSeatReservation(ticketRequest)

        expect(numberOfSeats).to.be.eq(2);
    })

    it('should return 5 if 2 adult, 3 child, 3 infant seat is requested', () => {
        ticketRequest = {
            ADULT: 2,
            CHILD: 3,
            INFANT: 3
        }

        numberOfSeats = calculateSeatReservation(ticketRequest)

        expect(numberOfSeats).to.be.eq(5);
    })
})
