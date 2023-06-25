import TicketPaymentService from '../src/thirdparty/paymentgateway/TicketPaymentService.js';

export default class MockTicketPaymentService extends TicketPaymentService {
  constructor() {
    super();
    this.paymentRequests = [];
    this.requestReceived = false;
  }

  requestPayment(accountId, amount) {
    this.paymentRequests.push({ accountId, amount });
    this.requestReceived = true;
  }
}
