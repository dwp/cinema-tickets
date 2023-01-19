import express from "express";

const app = express();

import TicketService from "../pairtest/TicketService.js";
import TicketTypeRequest from "../pairtest/lib/TicketTypeRequest.js";
import * as services from "../../test/service-objects.js";

// allow the app to use request body
app.use(express.json());

// Would add cors middleware here if connecting to a frontend to allow traffic from specific location

const port = process.env.port || 8080;

// Future iterations could include swagger to control the API contract
app.post("/tickets", function (req, res) { 

  // services to be injected when creating the new TicketService object
    const SRS = services.SRS;
    const TPS = services.TPS;
    const HELPER = services.HELPER;
    const ticketRequests = [];

    try {
      for(let tickreq of req.body.ticketRequests){
        let newTicketReq = new TicketTypeRequest(tickreq.ticketType, parseInt(tickreq.noOfTickets));
        ticketRequests.push(newTicketReq);
      }
      res.send({
        response: new TicketService(SRS, TPS, HELPER).purchaseTickets(parseInt(req.body.accountid),ticketRequests)
      })
    } catch (err) {
      res.status(500).send({
        error: err.message
      })
    }
    
})

app.listen(port, function () {
    console.log(`Example app listening on port ${port}!`)
})

export default app
