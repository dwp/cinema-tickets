# DWP Code Test

This is an implementation of a cinema ticketing application, completed as a coding exercise for DWP Digital. All code is in the `/cinema-tickets-java` directory, and the implementation is written using Java 17.

## Task

The outline of the task is provided in `TASK.md`.

### Approach

Initially, the requests are validated using the `TicketServiceValidator` based on the following requirements:

- Child and infant tickets cannot be requested without at least one adult ticket
- Each infant ticket must be accompanied by an adult ticket
- Only a maximum of 20 tickets can be requested at one time

A separate check that the account number is greater than 0 is also performed.

If the request **is not** valid, an exception specific to the issue is thrown and no requests are made to the third party services.

If the request **is** valid, the `purchaseTickets` method calculates the total cost of the ticket request and the number of seats to reserve by making use of  `TicketTypeRequest` methods, and makes calls to the `TicketPaymentService` and `SeatReservationService` third party services.

I have used dependency injection for the third party services, which makes it easy to mock the services in the unit tests.

#### Request Validation

I have created individual exceptions for each of the above cases by inheriting from the `InvalidRequestException` class.

When I first approached this task, I threw an `InvalidRequestException` with different messages for each case and wrote unit tests which asserted that the correct message was thrown. However, I changed approach as error messages can frequently change and any changes would require the unit tests to change as well, whereas the current solution will still be correct.

### Assumptions

- I assumed that only a single infant can sit on a single adults lap, so the number of adult tickets must be greater than or equal to the number of infant tickets in a request.

## Running the application using Docker

The test suite can be executed using the Dockerfile included in `cinema-tickets-java`:

### Prerequisites

- Docker

### How to run

From the root directory, run the following commands:

```bash
docker build ./cinema-tickets-java -t cinema-tickets
docker run -ti cinema-tickets mvn test
```

## Running the application using Maven

### Prerequisites

The following are required to run the application locally using Maven:

- Java 17
- Maven

### How to run

The tests can be executed using the following command, within the `cinema-tickets-java` directory:

```bash
mvn clean compile
mvn clean test
```

## Example usage

The service can be used as follows:

```java
// instantiate a ticket service object
TicketService ticketService = new TicketServiceImpl(new TicketPaymentServiceImpl(), new SeatReservationServiceImpl());

// create request objects
TicketTypeRequest adultRequest = new TicketTypeRequest(Type.ADULT, 2);
TicketTypeRequest childRequest = new TicketTypeRequest(Type.CHILD, 2);
TicketTypeRequest infantRequest = new TicketTypeRequest(Type.INFANT, 1);

long accountId = 123L;

// pass the request objects to the payment method
ticketService.purchaseTickets(accountId, adultRequest, childRequest, infantRequest);
```

The above example would output the following to the terminal:

```
<Date> <Time> uk.gov.dwp.uc.pairtest.TicketServiceImpl purchaseTickets
INFO: Making request to TicketPaymentService to take payment of £60
<Date> <Time> uk.gov.dwp.uc.pairtest.TicketServiceImpl purchaseTickets
INFO: Making request to SeatReservationService to reserve 4 seat(s)
```

## Challenges

### Mockito

I have not previously used Mockito so I had to spend some time understanding how Mockito works and how to mock the third party services in the unit tests.

### Design desicion

Initially, I decided to use a map for storing the number of tickets of each type made in each request, as it meant that I only had to iterate the `requests` once to create the map, after which each method could use that map.

However, I realised that the solution could be improved by creating a separate `TicketServiceValidator`, and also calculating the cost and how many seats to book for each `TicketTypeRequest` within the `TicketTypeRequest` class.

After making this change, the total cost of the tickets and the total number of seats to reserve are calculated using methods from `TicketTypeRequest` and Streams to iterate the requests vararg.

## Improvement points

- It would be interesting to convert this application into a Spring Boot service, which would accept JSON request objects and pass these to the `purchaseTickets` method to take payments and reserve seats using the third party services.
- More advanced logging would be beneficial, as it would give the user a better idea of what the application was doing during each stage. For example, logging could be added to the validator to inform users why a request is invalid.
