// SUT
import TicketTypeRequest from '../src/pairtest/lib/TicketTypeRequest';
/**
 * Business rules
 *
 * - 3 ticket types
 *      - Infant: £0, Child: £10, Adult: £20
 * - Max 20 tickets can be purchased in one transaction
 * - Infants must sit on an adults lap, are not allocated a seat
 * - Child and infant tickets cannot be purchased without an adult
 * - Accounts with an ID greater than 0 are valid
 * -
 */

describe('TicketTypeRequest tests', () => {
  it('should not accept any other ticket types other than Adult, Child, Infant', () => {
    expect(() => { new TicketTypeRequest('FOO', 8); }).toThrow(TypeError);
  });

  it('should not allow for a non-integer for the number of specific ticket types', () => {
    expect(() => { new TicketTypeRequest('CHILD', 'bar'); }).toThrow(TypeError);
  });

  it('should not allow for a non-integer for the number of specific ticket type00s', () => {
    expect(() => { new TicketTypeRequest('INFANT', 0.1); }).toThrow(TypeError);
  });

  it('should not allow for any negative ticket numbers to be purchased', () => {
    expect(() => { new TicketTypeRequest('ADULT', -3); }).toThrow(TypeError);
  });
});
