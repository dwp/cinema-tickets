import express from 'express';

import TicketService from './src/pairtest/TicketService.js';
import SeatReservationService from './src/thirdparty/seatbooking/SeatReservationService.js';
import TicketPaymentService from './src/thirdparty/paymentgateway/TicketPaymentService.js';
import TicketTypeRequest from './dist/pairtest/lib/TicketTypeRequest.js';
import log from './dist/pairtest/lib/Logger.js';

const app = express();
const port = process.env.PORT ?? 3000;

app.use(express.json());

const ticketService = new TicketService(new SeatReservationService(), new TicketPaymentService());

app.post('/tickets/purchase', (req, res) => {
  try {
    const accountId = Number.parseInt(req.body.accountId);
    const ticketRequests = req.body.tickets.map((request) => new TicketTypeRequest(request.type, Number.parseInt(request.count)));
    ticketService.purchaseTickets(accountId, ...ticketRequests);
    res.sendStatus(204);
  } catch ({ name, message }) {
    log.error(`${name}: ${message}`);
    res.status(400);
    res.send(message);
  }
})

// Health check route
app.get('/ping', (req, res) => {
  res.send('pong');
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})

export default app;
