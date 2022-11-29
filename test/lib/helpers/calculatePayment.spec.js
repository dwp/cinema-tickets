import { expect } from 'chai'
import calculatePayment from '../../../src/lib/helpers/calculatePayment.js'


describe('calculatePayment', () => {
    let ticketRequest;
    let total;
    it('should return 20 for one adult ticket', () => {
        ticketRequest = {
            ADULT: 1,
            CHILD: 0,
            INFANT: 0
        }
        
        total = calculatePayment(ticketRequest);

        expect(total).to.be.eq(20);
    });

    it('should return 30 for one adult one child ticket', () => {
        ticketRequest = {
             ADULT: 1,
             CHILD: 1,
             INFANT: 0
         }
         
         total = calculatePayment(ticketRequest);
 
         expect(total).to.be.eq(30);
     });

     it('should return 60 for 2 adult 2 child 2 infant', () => {
        ticketRequest = {
             ADULT: 2,
             CHILD: 2,
             INFANT: 2
         }
         
         total = calculatePayment(ticketRequest);
 
         expect(total).to.be.eq(60);
     });
})
