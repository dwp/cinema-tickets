# cinema-tickets
Coding exercise -  <br/> Java implementation of TicketService that validates the Ticket request, calculates the payment and seats and calls third party services for seat reservations and payment


## Requirement
Java 21 <br/>
Maven


## Running Tests
``
mvn test
``

## Assumptions
1. Account ID is greater than zero
2. Each request must contain at least 1 ticket
3. Third party services always succeed whenever requested

## Validations
1. TicketRequests array for null or empty
2. AccountID for null, 0 and negative values
3. Ticket type for null
4. Ticket quantity for 0 and negative values
5. Maximum number of allowed tickets 
6. Child/infant ticket requires an adult ticket

## Implementation
- Third party services are injected through Constructor  style dependency injection, this allows tests to mock these services
- Ticket prices and maximum allowed ticket quantity are defined as constants, a private PurchaseSummary record holds total number of seats and payment
- validation counters are defined as a long to avoid integer overflow
- Third party packages are unchanged
- TicketService interface is unchanged

## Test Scenarios
- Input validations
  - Invalid Requests
  - Invalid AccountID, ticket quantities
  - Maximum 25 tickets per purchase, including infants
  - Adult required for Child/infant purchase
- Calculations
  - for mixed types of tickets
  - same types of tickets
  - maximum possible amount 
- Edge scenarios 
  - Exact 25 tickets
  - Large quantities that could overflow int
  - Multiple tickets for same type
  - No payment or seat reservation calls for invalid requests

## Possible future improvement
Ticket prices and maximum allowed ticket quantity can be defined in configuration file if it is required to change independently of application
