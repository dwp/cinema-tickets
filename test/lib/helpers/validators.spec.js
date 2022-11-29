import { expect } from 'chai'

import { accountIDValidator, requestValidator } from '../../../src/lib/helpers/validators.js'

describe('validators', () => {
  describe('accountIDValidator', () => {
    it('should return no errors for a valid accountID', () => {
      expect(accountIDValidator(1)).to.be.deep.eq([])
    })

    it('should return an error when AccountID is null', () => {
      expect(accountIDValidator(null)).to.be.deep.eq(['AccountID is not provided!'])
    })

    it('should return an error when AccountID is undefined', () => {
      expect(accountIDValidator(undefined)).to.be.deep.eq(['AccountID is not provided!'])
    })

    it('should return an error when AccountID is not a number', () => {
      expect(accountIDValidator('13')).to.be.deep.eq(['AccountID must be a number'])
    })

    it('should return an error when AccountID is zero', () => {
      expect(accountIDValidator(0)).to.be.deep.eq(['AccountID cannot be zero!'])
    })
  })

  describe('requestValidator', () => {
    it('should return an empty array for a valid request', () => {
      const request = {
        ADULT: 4,
        CHILD: 0,
        INFANT: 0
      }

      expect(requestValidator(request)).to.be.deep.eq([])
    })

    it('should return an error if more than 20 tickets requested', () => {
      const request = {
        ADULT: 8,
        CHILD: 8,
        INFANT: 8
      }

      expect(requestValidator(request)).to.be.deep.eq(['Max number of tickets available to purchase at once is 20!'])
    })

    it('should return an error when children are not accompanied by an adult', () => {
      const request = {
        ADULT: 0,
        CHILD: 8,
        INFANT: 0
      }

      expect(requestValidator(request)).to.be.deep.eq(['Children must be accommodated by an adult!'])
    })

    it('should return both errors if infants and children are not accompanied by an adult', () => {
      const request = {
        ADULT: 0,
        CHILD: 8,
        INFANT: 8
      }

      expect(requestValidator(request)).to.be.deep.eq(['Infants and children must be accommodated by an adult!', "Too many infants! Each infant needs an adult's lap to sit on."])
    })

    it('should return 3 errors when 20+ infants and children are unaccompanied by an adult', () => {
      const request = {
        ADULT: 0,
        CHILD: 18,
        INFANT: 18
      }

      expect(requestValidator(request)).to.be.deep.eq(['Max number of tickets available to purchase at once is 20!', 'Infants and children must be accommodated by an adult!', "Too many infants! Each infant needs an adult's lap to sit on."])
    })
  })
})
