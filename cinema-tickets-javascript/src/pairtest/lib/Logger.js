import Winston from 'winston';

export default Winston.createLogger({
  transports: [
    new Winston.transports.Console(),
  ]
});
