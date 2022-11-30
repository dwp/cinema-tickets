// load up the express framework and body-parser helper
import express from 'express';
import bodyParser from 'body-parser';
import { router } from './routes/routes.js';
import * as dotenv from 'dotenv' 

dotenv.config();
const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// initialize express router
router(app);

const port = process.env.PORT || 8085
const server = app.listen(port, () => {
  console.log('listening on port %s...', server.address().port);
});