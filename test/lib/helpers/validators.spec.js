import { expect } from 'chai';

import { accountIDValidator } from '../../../src/lib/helpers/validators.js'

describe('validators', () => {
   describe('accountIDValidator', () => {
        it('should return no errors for a valid accountID', () => {
            expect(accountIDValidator(1)).to.be.deep.eq([]);
        });

        it('should return an error when AccountID is null', () => {
            expect(accountIDValidator(null)).to.be.deep.eq(['AccountID is not provided!'])
        });

        it('should return an error when AccountID is undefined', () => {
            expect(accountIDValidator(undefined)).to.be.deep.eq(['AccountID is not provided!'])
        })

        it('should return an error when AccountID is not a number', () => {
            expect(accountIDValidator('13')).to.be.deep.eq(['AccountID must be a number'])
        })

        it('should return an error when AccountID is zero', () => {
            expect(accountIDValidator(0)).to.be.deep.eq(['AccountID cannot be zero!'])
        })
   }); 
});