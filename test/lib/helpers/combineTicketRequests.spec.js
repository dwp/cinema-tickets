import { combineTicketRequests } from "../../../src/lib/helpers/combineTicketRequests.js";
import { assert, expect } from 'chai'
import TicketTypeRequest from "../../../src/lib/TicketTypeRequest.js";

describe('combineTicketRequests', () => {
    it('should throw an error if no requests passed', () => {
        assert.throws(() => {combineTicketRequests()}, 'Error in combineTicketRequests: no requests were provided!')
    });

    it('should handle a single ticket', () => {
        const requests = [new TicketTypeRequest('ADULT', 2)]
        const result = combineTicketRequests(requests);

        expect(result).to.be.deep.eq({
            ADULT: 2,
            CHILD: 0,
            INFANT: 0,
        })
    });

    it('should handle multiple different tickets', () => {
        const requests = [new TicketTypeRequest('ADULT', 2), new TicketTypeRequest('CHILD', 4), new TicketTypeRequest('INFANT', 1)]
        const result = combineTicketRequests(requests);

        expect(result).to.be.deep.eq({
            ADULT: 2,
            CHILD: 4,
            INFANT: 1,
        })
    })

    it('should handle multiple of the same tickets', () => {
        const requests = [new TicketTypeRequest('ADULT', 2), new TicketTypeRequest('CHILD', 4), new TicketTypeRequest('CHILD', 2)]
        const result = combineTicketRequests(requests);

        expect(result).to.be.deep.eq({
            ADULT: 2,
            CHILD: 6,
            INFANT: 0,
        })
    })
});