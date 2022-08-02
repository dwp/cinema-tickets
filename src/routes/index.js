export default ({
  app,
  C,
  express,
  handlers
}) => {
  // middleware to handle POST body
  app.use(express.json())

  // /healthcheck
  app
    .route(C.routes.healthcheck.path)
    .get(handlers.healthcheck)

  // /tickets
  app
    .route(C.routes.tickets.path)
    .post(handlers.tickets)
}
