export default ({
  C
}) => ({
  tickets
}) => {
  if (tickets.adult === undefined || tickets.child == undefined) {
    throw new ReferenceError('Invalid ticket request')
  }
  return (C.tickets.type.adult.price * tickets.adult)
    + (C.tickets.type.child.price * tickets.child)
}