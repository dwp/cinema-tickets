export default ({
  app,
  C,
  handlers
}) => {
  // /healthcheck
  app
    .route(C.routes.healthcheck.path)
    .get(handlers.healthcheck)

  // /tickets
  app
    .route(C.routes.tickets.path)
    .post(handlers.tickets)
}
