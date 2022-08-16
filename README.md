# DWP Code Test

This is an implementation of a cinema ticketing application, completed as a coding exercise for DWP Digital. All code is in the `/cinema-tickets-java` directory, and the implementation is written using Java 17.

## Task

The outline of the task is provided in `TASK.md`.

### Approach

Initially, the requests are validated based on the following requirements:

- Child and infant tickets cannot be requested without at least one adult ticket
- Each infant ticket must be accompanied by an adult ticket
- Account number must be greater than 0
- Only a maximum of 20 tickets can be requested at one time

If the request is not valid, an informative exception is thrown to explain to the user why the request was invalid.

If the request is valid, the `purchaseTickets` method calculates the total cost of the ticket request and the number of seats to reserve, and makes calls to the `TicketPaymentService` and `SeatReservationService` third party services.

The interfaces for these third party services have been used in the implementation so as not to rely on concrete classes, so if the concrete implementation of the third party services changes in the future it will not break the `purchaseTickets` method.

I have used dependency injection for the third party services, which allowed me to mock the services in the unit tests easily.

## Running the application

### Prerequisites

The following are required to run the application locally using Maven:

- Java 17
- Maven

### How to run

The tests can be executed using the following command, within the `cinema-tickets-java` directory:

```bash
mvn clean test
```

## Challenges

- I have not previously used Mockito so I had to spend some time understanding how Mockito works and how to mock the third party services in the unit tests.
- I had to make a design decision of whether to use a Map for storing the request details, or to use Streams/loops on the `requests` vararg. I decided to follow the Map approach as it meant that I only had to iterate the `requests` once to create the Map, after which each method used that map.

## Improvement points

- It would be interesting to convert this application into a Spring Boot service, which could accept JSON request objects and pass these to the `purchaseTickets` method to take payments and reserve seats using the third party services.
