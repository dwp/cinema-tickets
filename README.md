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

If the request **is** valid, the `purchaseTickets` method calculates the total cost of the ticket request and the number of seats to reserve by making use of `TicketTypeRequest` methods, and makes calls to the `TicketPaymentService` and `SeatReservationService` third party services.

Dependency injection was used for the third party services, which made it easy to mock the services in the unit tests.

### Assumptions

- Only a single infant can sit on a single adults lap, so the number of adult tickets must be greater than or equal to the number of infant tickets in a request.

## Running the application using Docker

### Prerequisites

- Docker

### How to run

The tests can be executed using the following commands within the root directory:

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

The tests can be executed using the following commands within the `cinema-tickets-java` directory:

```bash
mvn clean compile
mvn clean test
```

## Example usage

An example case for a purchase request for 2 adults, 2 children, and 1 infant is shown below:

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

The above example would output the following:

```
<Date> <Time> uk.gov.dwp.uc.pairtest.TicketServiceImpl purchaseTickets
INFO: Making request to TicketPaymentService to take payment of £60
<Date> <Time> uk.gov.dwp.uc.pairtest.TicketServiceImpl purchaseTickets
INFO: Making request to SeatReservationService to reserve 4 seat(s)
```

## Challenges

### Mockito

I have not previously used Mockito, so I had to spend some time reading the Mockito documentation and looking at examples of how it can be used to mock dependencies. Whilst writing the unit tests was initially challenging, the documentation was helpful for understanding how to use this framework and I would now be more confident using Mockito in the future.

### Design desicion

Initially, I decided to use a map for storing the number of tickets of each type made in each request, as it meant that I only had to iterate the `requests` once to create the map, after which each method could use that map.

However, whilst refactoring the solution I realised that it could be improved by creating a separate `TicketServiceValidator`, and also calculating the cost and how many seats to book for each `TicketTypeRequest` within the `TicketTypeRequest` class.

After making these changes, the total cost of the tickets and the total number of seats to reserve are calculated using methods from `TicketTypeRequest` and Streams to iterate the requests vararg.

## Improvement points

- This application could be converted into a Spring Boot service, which would accept JSON request objects and pass these to the `purchaseTickets` method to take payments and reserve seats using the third party services.
- More advanced logging would be beneficial, as it would give the user a better idea of what the application was doing during each step. For example, logging could be added to the validator to inform users why a request is invalid.
