module.exports = ({
  app,
  host,
  port,
  startMsg
}) => () => {
  try {
    app.listen(port, host)
    console.log(`${startMsg} ${host}:${port}`)
  } catch (error) {
    console.error(error)
    process.exit(1)
  }
}
