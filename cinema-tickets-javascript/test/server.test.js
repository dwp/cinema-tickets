import chai from 'chai';
import { expect } from 'chai';
import chaiHttp from 'chai-http';

import server from '../server.js';
import { ADULT, CHILD, INFANT } from '../dist/pairtest/lib/Constants.js';

chai.use(chaiHttp);

describe('Ticket server', () => {
  describe('Health check route', () => {
    it('should return status 200', async () => {
      const response = await chai.request(server).get('/ping');
      expect(response).to.have.status(200);
    });

    it('should return pong in body', async () => {
      const response = await chai.request(server).get('/ping');
      expect(response.text).to.eq('pong');
    });
  });

  describe('Ticket purchase route', () => {
    it('should return status 204 with valid request', async () => {
      const request = {
        accountId: 1,
        tickets: [
          {
            type: ADULT,
            count: 5
          }
        ]
      };

      const response = await chai.request(server).post('/tickets/purchase').send(request);
      expect(response).to.have.status(204);
    });

    it('should return status 400 with invalid request', async () => {
      const request = {
        accountId: 0,
        tickets: [
          {
            type: ADULT,
            count: 5
          }
        ]
      };

      const response = await chai.request(server).post('/tickets/purchase').send(request);
      expect(response).to.have.status(400);
    });

    it('should return correct error message for too many tickets', async () => {
      const request = {
        accountId: 1,
        tickets: [
          {
            type: ADULT,
            count: 21
          }
        ]
      };

      const response = await chai.request(server).post('/tickets/purchase').send(request);
      expect(response.text).to.eq('Total number of tickets must be less than 20');
    });

    it('should return correct error message for incorrect account ID', async () => {
      const request = {
        accountId: 0,
        tickets: [
          {
            type: ADULT,
            count: 5
          }
        ]
      };

      const response = await chai.request(server).post('/tickets/purchase').send(request);
      expect(response.text).to.eq('Account ID must be greater than 0');
    });

    it('should return correct error message when child tickets in request with no adult tickets', async () => {
      const request = {
        accountId: 1,
        tickets: [
          {
            type: CHILD,
            count: 1
          }
        ]
      };

      const response = await chai.request(server).post('/tickets/purchase').send(request);
      expect(response.text).to.eq('Cannot purchase child or infant tickets without purchasing an adult ticket');
    });

    it('should return correct error message when number of infant tickets greater than adult tickets', async () => {
      const request = {
        accountId: 1,
        tickets: [
          {
            type: ADULT,
            count: 2
          },
          {
            type: INFANT,
            count: 3
          }
        ]
      };

      const response = await chai.request(server).post('/tickets/purchase').send(request);
      expect(response.text).to.eq('Cannot purchase more infant tickets than adult tickets');
    });
  });
});
