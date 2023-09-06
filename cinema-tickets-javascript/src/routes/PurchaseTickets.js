import TicketTypeRequest from '../../dist/pairtest/lib/TicketTypeRequest.js';

export default (ticketService, req, res, log) => {
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
}
