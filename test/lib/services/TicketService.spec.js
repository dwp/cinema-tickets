import * as sinon from 'sinon';
import TicketPaymentService from '../../../src/thirdparty/paymentgateway/TicketPaymentService.js';
import TicketService from '../../../src/lib/services/TicketService.js';

describe('TicketService', () => {
    let makePaymentStub;
    let ticketService;

    beforeEach(() => {
        makePaymentStub = sinon.stub(TicketPaymentService.prototype, 'makePayment').returns(true);
        ticketService = new TicketService();
    });

    afterEach(() => {
        makePaymentStub.restore();
    });

    context('purchaseTickets', () => {
        it('should call makePayment method of ticketPaymentService', () => {
            ticketService.purchaseTickets(1, 0);
            sinon.assert.calledOnceWithExactly(makePaymentStub, 1, 0);
        });
    });
});