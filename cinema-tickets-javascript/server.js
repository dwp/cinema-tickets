import express from 'express';

import TicketService from './src/pairtest/TicketService.js';
import SeatReservationService from './src/thirdparty/seatbooking/SeatReservationService.js';
import TicketPaymentService from './src/thirdparty/paymentgateway/TicketPaymentService.js';
import purchaseTicketsPost from './src/routes/PurchaseTickets.js';
import log from './src/pairtest/lib/Logger.js';

const app = express();
// eslint-disable-next-line no-undef
const port = process.env.PORT ?? 3000;
app.use(express.json());

const ticketService = new TicketService(new SeatReservationService(), new TicketPaymentService(), log);

app.post('/tickets/purchase', (req, res) => {
  purchaseTicketsPost(ticketService, req, res, log);
})

// Health check route
app.get('/ping', (req, res) => {
  res.send('pong');
})

app.listen(port, () => {
  log.info(`Application listening on port ${port}`);
})

export default app;
