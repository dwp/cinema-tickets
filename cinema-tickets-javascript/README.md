#Cinema Tickets

A ticket purchasing service that validates requests.
Calculates payments and reserves seats.

##Installation
npm install

##Running Tests
npm test

## Approach

- TDD — tests written before implementation
- `TicketService.purchaseTickets()` is the only public method; validation, pricing, and seat calculation are separated into private methods
- Thirdparty services are mocked in tests to verify correct payment amounts and seat reservations

## Business Rules Implemented

- Only ADULT, CHILD, and INFANT ticket types accepted
- Maximum 25 tickets per purchase
- At least one ADULT ticket required
- Infants are free and do not get a seat
- Account ID must be a positive integer
