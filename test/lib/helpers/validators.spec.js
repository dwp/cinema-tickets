import { expect } from 'chai';

import { accountIDValidator } from '../../../src/lib/helpers/validators.js'

describe('validators', () => {
   describe('accountIDValidator', () => {
        it('should return no errors for a valid accountID', () => {
            const result = accountIDValidator(1);
            expect(result).to.be.deep.eq([]);
        });
   }); 
});