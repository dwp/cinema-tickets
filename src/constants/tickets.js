const ADULT = 'ADULT'
const CHILD = 'CHILD'
const INFANT = 'INFANT'

export default {
  allowedTypes: [ADULT, CHILD, INFANT],
  type: {
    adult: {
      price: 20,
      type: ADULT,
      usesSeat: true
    },
    child: {
      price: 10,
      type: CHILD,
      usesSeat: true
    },
    infant: {
      price: 0,
      type: INFANT,
      usesSeat: false
    }
  }
}
