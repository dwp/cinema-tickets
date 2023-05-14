import { ticketsHandler } from '../lib/handlers/ticketsHandler.js'

export function ticketRoutes (app) {
  app.post('/tickets', (req, res) => {
    try {
      const reserveSeatsResponse = ticketsHandler(req.body)
      return res.send(reserveSeatsResponse)
    } catch (err) {
      return res.status(err.statusCode).json({
        code: err.statusCode || 500,
        message: err.message,
      })
    }
  })
}
