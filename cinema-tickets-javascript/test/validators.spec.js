// SUT
import {
  accountIdGreaterThanZero,
  infantNotGreaterThanAdult,
  maxTwentyTicketsAllowed,
  atLeastOneAdult,
} from '../src/pairtest/validators';

describe('validators tests', () => {
  describe('accountIdGreaterThanZero', () => {
    it('should return valid if accountId is greater than 0', () => {
      expect(accountIdGreaterThanZero({ accountId: 100000 }).valid).toBe(true);
    });

    it('should return invalid if accountId is less than 0', () => {
      expect(accountIdGreaterThanZero({ accountId: -100000 }).valid).toBe(false);
    });

    it('should return an error if accountId is less than 0', () => {
      expect(accountIdGreaterThanZero({ accountId: -900000 }).error).toBeTruthy();
    });
  });

  describe('infantNotGreaterThanAdult', () => {
    it('should return valid if adultTickets are greater than infantTickets', () => {
      expect(infantNotGreaterThanAdult({ adultTickets: 5, infantTickets: 4 }).valid).toBe(true);
    });

    it('should return valid if adultTickets equal to infantTickets', () => {
      expect(infantNotGreaterThanAdult({ adultTickets: 4, infantTickets: 4 }).valid).toBe(true);
    });

    it('should return invalid if adultTickets are less than infantTickets', () => {
      expect(infantNotGreaterThanAdult({ adultTickets: 1, infantTickets: 4 }).valid).toBe(false);
    });

    it('should return an error if adultTickets are less than infantTickets', () => {
      expect(infantNotGreaterThanAdult({ adultTickets: 2, infantTickets: 3 }).error).toBeTruthy();
    });
  });

  describe('maxTwentyTicketsAllowed', () => {
    it('should return valid total tickets are less than 20', () => {
      expect(maxTwentyTicketsAllowed({ adultTickets: 5, childTickets: 7, infantTickets: 4 }).valid).toBe(true);
    });

    it('should return valid if total tickets are equal to 20', () => {
      expect(maxTwentyTicketsAllowed({ adultTickets: 15, childTickets: 1, infantTickets: 4 }).valid).toBe(true);
    });

    it('should return invalid if total tickets are greater than 20', () => {
      expect(maxTwentyTicketsAllowed({ adultTickets: 5, childTickets: 17, infantTickets: 4 }).valid).toBe(false);
    });

    it('should return an error if total tickets are greater than 20', () => {
      expect(maxTwentyTicketsAllowed({ adultTickets: 10000 }).error).toBeTruthy();
    });
  });

  describe('atLeastOneAdult', () => {
    it('should return valid if there is one adult', () => {
      expect(atLeastOneAdult({ adultTickets: 1, childTickets: 7, infantTickets: 1 }).valid).toBe(true);
    });

    it('should return invalid if there are no adults', () => {
      expect(atLeastOneAdult({ childTickets: 1, infantTickets: 4 }).valid).toBe(false);
    });

    it('should return an error if total tickets are greater than 20', () => {
      expect(maxTwentyTicketsAllowed({ adultTickets: 0, childTickets: 8 }).error).toBeTruthy();
    });
  });
});
