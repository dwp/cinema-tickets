import request from "supertest";
import app from "./index.js";
describe("simple integration test", () => {

    it("should return a 200 for a properly structured request body", (done) => {
        const response = request(app)
          .post("/tickets")
          .send({
            "accountid": 123,
            "ticketRequests": [
                {
                    "ticketType": "ADULT",
                    "noOfTickets": 1
                }
            ]})
            .set("Content-Type", "application/json")
            .expect(200)
            .end(function(err, res) {
                if (err) return done(err);
                return done();
              });
        
      });

      it("should return a 500 for a poorly structured request body", (done) => {
        const response = request(app)
          .post("/tickets")
          .send({
            "accountid": "FUDGE",
            "ticketRequests": [
                {
                    "ticketType": "ADULT",
                    "noOfTickets": 1
                }
            ]})
            .set("Content-Type", "application/json")
            .expect(500)
            .end(function(err, res) {
                if (err) return done(err);
                return done();
              });
        
      });
})
