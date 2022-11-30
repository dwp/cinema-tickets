import { ticketsHandler } from '../lib/handlers/ticketsHandler.js'

export function ticketRoutes (app) {
  app.post('/tickets', (req, res) => {
    const reserveSeatsResponse = ticketsHandler(req.body)
    return res.send(reserveSeatsResponse)
  })
}
