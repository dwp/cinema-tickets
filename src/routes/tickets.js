import { ticketsHandler } from '../lib/handlers/ticketsHandler.js'

export function ticketRoutes (app) {
  app.post('/tickets', (req, res, next) => {
    try {
      const reserveSeatsResponse = ticketsHandler(req.body)
      return res.send(reserveSeatsResponse)
    } catch (err) {
      next(err.message)
    }
  })
}
