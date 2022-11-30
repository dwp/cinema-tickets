import sinon from "sinon";
import { ticketRoutes } from "../../src/routes/tickets.js";

describe('ticketRoutes', () => {
    let app;

    beforeEach(() => {
        app = {
            post: sinon.spy()
        }
        ticketRoutes(app);
    });
    it('should call post route', () => {
        sinon.assert.calledOnceWithMatch(app.post, '/tickets', Function)
    });
});