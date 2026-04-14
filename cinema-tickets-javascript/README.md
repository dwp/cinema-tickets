# Cinema Tickets - TicketService Code Test

## Overview

This project provides an implementation of the `TicketService` responsible for:

- Validating ticket purchase requests
- Calculating total payment amount
- Determining the number of seats to reserve
- Interacting with external services for payment and seat reservation

The implementation follows clean code principles and is covered by unit tests developed using a Test-Driven Development (TDD) approach. The tests are written in a behaviour-focused style to clearly express the business rules.

---

## Approach

The solution was developed incrementally using TDD:

1. Write a failing test
2. Implement the smallest change to make it pass
3. Refactor while keeping tests green

The test suite is organised by behaviour:

- validation
- payment calculation
- seat reservation
- service interaction

This structure helps communicate the expected behaviour of the system clearly.

---

## Design Decisions

### Separation of Concerns

The `purchaseTickets` method acts as a high-level workflow:

- Input validation
- Request summarisation
- Business rule validation
- Calculation of totals
- Calling external services

Supporting logic is extracted into private helper methods to keep the main method readable and focused.

---

### Dependency Injection

External services (`TicketPaymentService` and `SeatReservationService`) are injected via the constructor.

This allows:

- Easy mocking during testing
- Decoupling from concrete implementations
- Improved testability

---

### Validation Strategy

Validation is performed early to:

- Prevent invalid data from being processed
- Avoid unnecessary calls to external systems

Business rules enforced include:

- Valid account ID
- At least one ticket requested
- Ticket quantities must be greater than zero
- Child and infant tickets require an adult
- Maximum of 25 tickets per purchase
- Infants cannot exceed the number of adults

---

## Pricing Rules

| Ticket Type | Price |
| ----------- | ----- |
| INFANT      | £0    |
| CHILD       | £15   |
| ADULT       | £25   |

- Infants do not require seats and are not charged
- Seats are only allocated for adults and children

---

## Running the Project

### Install dependencies

```bash
npm install
```

### Run Tests

```bash
npm test
```

## Reflections

This project involved working with patterns such as dependency injection and class-based design, which I do not use as frequently in my day-to-day work making it a useful opportunity to reinforce these concepts in a practical scenario.
