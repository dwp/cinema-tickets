import chai from 'chai';
import InvalidPurchaseException from '../src/pairtest/lib/InvalidPurchaseException.js';

const { expect } = chai;

describe('InvalidPurchaseException', () => {
  it('should be an instance of Error', () => {
    const exception = new InvalidPurchaseException('Test message');

    expect(exception).to.be.instanceOf(Error);
  });

  it('should have the correct name', () => {
    const exception = new InvalidPurchaseException('Test message');

    expect(exception.name).to.equal('InvalidPurchaseException');
  });

  it('should set the provided message', () => {
    const message = 'Test message';
    const exception = new InvalidPurchaseException(message);

    expect(exception.message).to.equal(message);
  });
});
