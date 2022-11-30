// load up the express framework and body-parser helper
import express from 'express';
import bodyParser from 'body-parser';
import { router } from './routes/routes.js';

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// initialize express router
router(app);

const server = app.listen(8085, () => {
  console.log('listening on port %s...', server.address().port);
});